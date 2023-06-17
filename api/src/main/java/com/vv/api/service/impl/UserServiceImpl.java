package com.vv.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.common.RabbitmqUtils;
import com.vv.api.common.RedisTokenBucket;
import com.vv.api.mapper.UserMapper;
import com.vv.api.model.dto.LoginByEmailDTO;
import com.vv.api.model.dto.LoginByPhoneDTO;
import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.dto.user.UserQueryRequest;
import com.vv.api.model.po.User;
import com.vv.api.service.UserService;
import com.vv.common.constant.RedisConstant;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.to.SmsTo;
import com.vv.common.enums.ResponseCode;
import com.vv.common.utils.AuthUtils;
import com.vv.common.utils.CheckUtils;
import com.vv.common.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author vv
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    private RedisTokenBucket redisTokenBucket;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private RabbitmqUtils rabbitmqUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    /**
     * 发一条短信到手机，并且将验证码放到redis中
     * @param phone
     * @return
     */
    @Override
    public boolean registerSms(String phone) {
        //先看看你这个b是不是已经发过短信了
//        String code = (String) redisTemplate.opsForValue().get(RedisConstant.REGISTER_CODE_PREFIX+phone);
//        if(code != null){
//            return false;
//        }
        if(!redisTokenBucket.tryAcquire(phone)){
            return false;
        }
        //生成验证码
        String newCode = RandomUtil.randomNumbers(5);
        //消息队列发送短信到手机
        SmsTo smsTo = new SmsTo(phone, newCode);
        rabbitmqUtils.sendSms(smsTo);
        return true;
    }

    /**
     * 发一条短信到手机，并且将验证码放到redis中
     * @param phone
     * @return
     */
    @Override
    public boolean loginSms(String phone) {
        //先看看你这个b是不是已经发过短信了
//        String code = (String) redisTemplate.opsForValue().get(RedisConstant.LOGIN_CODE_PREFIX+phone);
//        if(code != null){
//            return false;
//        }
        if(!redisTokenBucket.tryAcquire(phone)){
            return false;
        }
        //生成验证码
        String newCode = RandomUtil.randomNumbers(5);
        //消息队列发送短信到手机
        SmsTo smsTo = new SmsTo(phone, newCode);
        rabbitmqUtils.sendSms(smsTo);
        return true;
    }

    @Override
    public long userRegister(RegisterUserDTO registerUserDTO) {
        String phone = registerUserDTO.getUserPhone();
        //1.手机是否合法
        if (phone == null || phone.length() < 8 || !CheckUtils.isPhoneNum(phone)) {
            throw new BusinessException(ResponseCode.SMS_CODE_ERROR,"手机非法");
        }
        //2.邮箱格式是否正确
        String email = registerUserDTO.getUserEmail();
        if(!CheckUtils.isEmail(email)){
            throw new BusinessException(ResponseCode.EMAIL_ERROR,"邮箱非法");
        }
        //3.确认密码和密码是否相同、长度是否符合规则
        String password = registerUserDTO.getPassword();
        String confirmPassword = registerUserDTO.getConfirmPassword();
        if(password.length() < 8 || confirmPassword.length() < 8 | !password.equals(confirmPassword)){
            throw new BusinessException(ResponseCode.PASSWORD_NO_MATCH_ERROR,"密码二次不匹配");
        }
        //4.确认发送的短信码是否一致
        //从Redis中获取短信验证码
        String code = (String) redisTemplate.opsForValue().get(RedisConstant.REGISTER_CODE_PREFIX + phone);

        if (code == null || !code.equals(registerUserDTO.getSmsCode())) {
            throw new BusinessException(ResponseCode.SMS_CODE_ERROR, "验证码错误");
        }
        //成功，从redis中移除这个验证码
        redisTemplate.delete(RedisConstant.REGISTER_CODE_PREFIX + phone);

        synchronized (phone.intern()){
            //5.是否已经注册
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userPhone",phone);
            long count = baseMapper.selectCount(queryWrapper);
            if(count > 0){
                throw new BusinessException(ResponseCode.USER_EXIST_ERROR,"用户已存在");
            }

            //6.注册成功
            User user = new User();
            //对密码进行加密
            String encodePassword = passwordEncoder.encode(registerUserDTO.getPassword());
            user.setUserEmail(email);
            user.setUserPhone(phone);
            user.setPassword(encodePassword);
            //生成accessKey
            String accessKey = authUtils.accessKey(phone);
            user.setAccessKey(accessKey);
            //生成secretKey
            String secretKey = authUtils.secretKey(phone, user.getUserEmail());
            user.setSecretKey(secretKey);
            //生成Token
            String token = authUtils.token(phone, user.getUserEmail(), accessKey,secretKey);

            user.setToken(token);

            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());

            //插入数据库
            save(user);

            return user.getId();
        }
    }

    @Override
    public SafeUserDTO loginByEmail(LoginByEmailDTO loginByEmailDTO,  HttpServletResponse response) {
        String email = loginByEmailDTO.getUserEmail();
        if(!CheckUtils.isEmail(email)){
            throw new BusinessException(ResponseCode.EMAIL_ERROR);
        }
        String password = loginByEmailDTO.getPassword();

        User user = ((User) loadUserByUsername(email));
        if(user == null){
            throw new BusinessException(ResponseCode.EMAIL_ERROR);
        }
        //对密码加密后进行匹配
        boolean isMatches = passwordEncoder.matches(loginByEmailDTO.getPassword(), user.getPassword());
        if(!isMatches){
            throw new BusinessException(ResponseCode.PASSWORD_ERROR);
        }
        return initLoginUser(user,response);
    }

    @Override
    public SafeUserDTO loginByPhone(LoginByPhoneDTO loginByPhoneDTO,  HttpServletResponse response) {
        String phone = loginByPhoneDTO.getUserPhone();
        //1.手机是否合法
        if (phone.length() < 8 || !CheckUtils.isPhoneNum(phone)) {
            throw new BusinessException(ResponseCode.SMS_CODE_ERROR,"手机非法");
        }
        //从Redis中获取短信验证码
        String code = (String) redisTemplate.opsForValue().get(RedisConstant.LOGIN_CODE_PREFIX + phone);

        if (code == null || !code.equals(loginByPhoneDTO.getSmsCode())) {
            throw new BusinessException(ResponseCode.SMS_CODE_ERROR, "验证码错误");
        }
        //成功，从redis中移除这个验证码
        redisTemplate.delete(RedisConstant.REGISTER_CODE_PREFIX + phone);
        //5.是否已经注册
        User user = ((User) loadUserByUsername(phone));
        if(user == null){
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR,"用户未注册");
        }

        return initLoginUser(user,response);
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        JWT jwt = JWTUtil.parseToken(token);
        String id = (String) jwt.getPayload("userId");
        //去掉redis中的用户信息
        redisTemplate.delete(RedisConstant.USER_INFO_PREFIX+id);
        return true;
    }

    /**
     * 分页获取用户列表
     * @param userQueryRequest
     * @return
     */
    @Override
    public Page<SafeUserDTO> listUserByPage(UserQueryRequest userQueryRequest) {
        long current = 1;
        long size = 10;
        User userQuery = new User();

        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = this.page(new Page<>(current, size), queryWrapper);
        Page<SafeUserDTO> userVoPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());

        List<SafeUserDTO> userVoList = userPage.getRecords().stream().map(user -> {
            SafeUserDTO userVo = new SafeUserDTO();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());

        userVoPage.setRecords(userVoList);

        return userVoPage;
    }


    /**
     * 根据 手机或者邮箱 获取用户
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userPhone",s).or().eq("userEmail",s);
        User user = getOne(queryWrapper);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    public SafeUserDTO initLoginUser(User user , HttpServletResponse response){
        SafeUserDTO safeUserDTO = new SafeUserDTO();
        BeanUtils.copyProperties(user,safeUserDTO);
        String token = tokenUtils.getToken(user.getId(), user.getUserEmail());
        safeUserDTO.setToken(token);
        //将Token存入到请求头中
        response.setHeader("token",token);
        String key = RedisConstant.USER_INFO_PREFIX + user.getId();
        //将用户信息存放到Redis中
        redisTemplate.opsForValue().set(key,safeUserDTO);
        //设置用户过期时间
        redisTemplate.expire(key,RedisConstant.USER_INFO_TTL, TimeUnit.SECONDS);
        //返回脱敏用户
        return safeUserDTO;
    }

}




