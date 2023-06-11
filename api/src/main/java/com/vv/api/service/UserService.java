package com.vv.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.dto.LoginByEmailDTO;
import com.vv.api.model.dto.LoginByPhoneDTO;
import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.dto.user.UserQueryRequest;
import com.vv.api.model.po.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author vv
*/
public interface UserService extends IService<User> {
    long userRegister(RegisterUserDTO registerUserDTO);

    boolean registerSms(String phone);

    SafeUserDTO loginByEmail(LoginByEmailDTO loginByEmailDTO, HttpServletResponse response);

    SafeUserDTO loginByPhone(LoginByPhoneDTO loginByPhoneDTO, HttpServletResponse response);

    boolean logout(HttpServletRequest request);

    boolean loginSms(String phone);

    Page<SafeUserDTO> listUserByPage(UserQueryRequest queryRequest);

}
