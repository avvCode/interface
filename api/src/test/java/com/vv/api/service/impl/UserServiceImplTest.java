package com.vv.api.service.impl;

import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.service.UserService;
import com.vv.common.constant.RabbitmqConstant;
import com.vv.common.utils.TokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    TokenUtils tokenUtils;

    @Resource
    UserService userService;
    @Test
    public void testToken(){
        String userAccount = "123456";
        int userId = 1;
        String token = tokenUtils.getToken(userId, userAccount);
        System.out.println("token = " + token);
        boolean b = tokenUtils.verifyToken(token);
        System.out.println("b = " + b);
    }
    @Test
    @RabbitListener(queues = {RabbitmqConstant.SMS_QUEUE_NAME})
    public void testSendSmsCode(){

    }
    @Test
    public void testRegister(){
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        String userEmail = "80141182@qq.com";
        String password = "12345678";
        String confirmPassword = "12345678";
        registerUserDTO.setUserEmail(userEmail);
        registerUserDTO.setPassword(password);
        registerUserDTO.setConfirmPassword(confirmPassword);
        registerUserDTO.setSmsCode("1234");
        long l = userService.userRegister(registerUserDTO);
    }
}