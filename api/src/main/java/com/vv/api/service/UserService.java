package com.vv.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.dto.LoginByEmailDTO;
import com.vv.api.model.dto.LoginByPhoneDTO;
import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.po.User;

/**
* @author vv
*/
public interface UserService extends IService<User> {
    long userRegister(RegisterUserDTO registerUserDTO);

    boolean registerSms(String phone);

    SafeUserDTO loginByEmail(LoginByEmailDTO loginByEmailDTO);

    SafeUserDTO loginByPhone(LoginByPhoneDTO loginByPhoneDTO);

    boolean loginSms(String phone);
}
