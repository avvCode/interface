package com.vv.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.dto.RegisterUser;
import com.vv.api.model.dto.SafeUser;
import com.vv.api.model.po.User;

/**
* @author vv
*/
public interface UserService extends IService<User> {
    SafeUser userRegister(RegisterUser registerUser);

    boolean registerPhone(String phone);
}
