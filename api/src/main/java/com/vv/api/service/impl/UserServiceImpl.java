package com.vv.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.common.RabbitmqUtils;
import com.vv.api.mapper.UserMapper;
import com.vv.api.model.dto.LoginByEmailDTO;
import com.vv.api.model.dto.LoginByPhoneDTO;
import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.po.User;
import com.vv.api.service.UserService;
import com.vv.common.constant.RedisConstant;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.to.SmsTo;
import com.vv.common.model.vo.ResponseCode;
import com.vv.common.utils.AuthUtils;
import com.vv.common.utils.CheckUtils;
import com.vv.common.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author vv
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private RabbitmqUtils rabbitmqUtils;

    private final String PREFIX_USER = "api:user:";
    /**
     * 发一条短信到手机，并且将验证码放到redis中
     * @param phone
     * @return
     */
    @Override
    public boolean registerSms(String phone) {
        //先看看你这个b是不是已经发过短信了
        String code = (String) redisTemplate.opsForValue().get(phone);
        if(code != null){
            return false;
        }
        //生成验证码
        String newCode = RandomUtil.randomString(5);
        //消息队列发送短信到手机
        SmsTo smsTo = new SmsTo(phone, newCode);
        rabbitmqUtils.sendSms(smsTo);
        return true;
    }

    @Override
    public long userRegister(RegisterUserDTO registerUserDTO) {
        String phone = registerUserDTO.getUserPhone();
        //1.手机是否合法
        if (phone.length() < 8 || !CheckUtils.isPhoneNum(phone)) {
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

        //5.是否已经注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userPhone",phone);
        long count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ResponseCode.USER_EXIST_ERROR,"用户已存在");
        }

        //6.注册成功
        User user = new User();

        //对密码进行md5加密
        Digester md5 = new Digester(DigestAlgorithm.MD5);

        String passwordMd5 = md5.digestHex(registerUserDTO.getPassword());

        user.setPassword(passwordMd5);

        //生成accessKey
        String accessKey = authUtils.accessKey(phone);
        user.setAccessKey(accessKey);
        //生成secretKey
        String secretKey = authUtils.secretKey(phone, user.getUserEmail());
        user.setSecretKey(secretKey);
        //生成Token
        String token = authUtils.token(phone, user.getUserEmail(), accessKey,secretKey);

        user.setToken(token);
        //插入数据库
        this.save(user);

        return user.getId();
    }

    @Override
    public SafeUserDTO loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        return null;
    }

    @Override
    public SafeUserDTO loginByPhone(LoginByPhoneDTO loginByPhoneDTO) {
        return null;
    }


    @Override
    public boolean loginSms(String phone) {
        return false;
    }
}




