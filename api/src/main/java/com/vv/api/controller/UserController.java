package com.vv.api.controller;

import com.vv.api.model.dto.RegisterUser;
import com.vv.api.model.dto.SafeUser;
import com.vv.api.service.UserService;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.vo.BaseResponse;
import com.vv.common.model.vo.ErrorCode;
import com.vv.common.model.vo.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    //TODO
    /**
     * 用户通过账号密码登录
     * @return
     */
    @PostMapping("/loginByAccount")
    public BaseResponse loginByAccount(){

        return ResultUtils.success(1);
    }
    //TODO
    /**
     * 用户通过手机+验证码登录
     */
    @PostMapping("/loginByPhone")
    public BaseResponse loginByPhone(){

        return ResultUtils.success(1);
    }
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterUser registerUser){
        if(registerUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        SafeUser safeUser = userService.userRegister(registerUser);
        if(safeUser == null){
            return ResultUtils.error(ErrorCode.REGISTER_ERROR,"注册失败");
        }
        return ResultUtils.success(safeUser);
    }
    /**
     * 用户注册时发送一条短信
     */
    @GetMapping("/registerPhone")
    public BaseResponse sendCode(@RequestParam String phone){
        if(phone == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }

        return ResultUtils.success(ErrorCode.SUCCESS);
    }
}
