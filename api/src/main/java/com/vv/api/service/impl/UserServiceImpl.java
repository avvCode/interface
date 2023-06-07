package com.vv.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.mapper.UserMapper;
import com.vv.api.model.dto.RegisterUser;
import com.vv.api.model.dto.SafeUser;
import com.vv.api.model.po.User;
import com.vv.api.service.UserService;
import com.vv.common.exception.BusinessException;
import com.vv.common.utils.CheckUtils;
import com.vv.common.model.vo.ErrorCode;
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

    private final String PREFIX_USER = "user:";
    /**
     * 发一条短信到手机，并且将验证码放到redis中
     * @param phone
     * @return
     */
    @Override
    public boolean registerPhone(String phone) {
        //先看看你这个b是不是已经发过短信了
        String code = (String) redisTemplate.opsForValue().get(phone);
        if(code != null){
            return false;
        }
        //生成验证码
        String newCode = RandomUtil.randomString(5);
        //TODO 消息队列发送短信到手机

        return true;
    }

    @Override
    public SafeUser userRegister(RegisterUser registerUser) {
        String phone = registerUser.getUserPhone();
        //1.手机是否合法
        if (!CheckUtils.isPhoneNum(phone)) {
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR,"手机非法");
        }
        //2.邮箱格式是否正确
        String email = registerUser.getUserEmail();
        if(!CheckUtils.isEmail(email)){
            throw new BusinessException(ErrorCode.EMAIL_ERROR,"邮箱非法");
        }
        //3.确认密码和密码是否相同
        String password = registerUser.getPassword();
        String confirmPassword = registerUser.getConfirmPassword();
        if(!password.equals(confirmPassword)){
            throw new BusinessException(ErrorCode.PASSWORD_NO_MATCH_ERROR,"密码二次不匹配");
        }
        //4.确认发送的短信码是否一致
        //TODO 从Redis中获取验证码
        String code = (String) redisTemplate.opsForValue().get(phone);

        //TODO 判断两个验证码是否一致 或者超时
        if(!code.equals(registerUser.getSmsCode())){
            //
        }
        //5.是否已经注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userPhone",phone);
        User one = baseMapper.selectOne(queryWrapper);
        if(one != null){
            throw new BusinessException(ErrorCode.USER_EXIST_ERROR,"用户已存在");
        }
        //6.注册成功
        User user = new User();
        //对密码进行md5加密
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String passwordMd5 = md5.digestHex(registerUser.getPassword());
        user.setPassword(passwordMd5);
        //TODO 生成Token
        //TODO 生成accessKey
        //TODO 生成secretKey
        this.save(user);
        SafeUser safeUser = new SafeUser();

        return safeUser;
    }
}




