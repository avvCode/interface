package com.vv.api.controller;

import com.vv.api.model.dto.LoginByEmailDTO;
import com.vv.api.model.dto.LoginByPhoneDTO;
import com.vv.api.model.dto.RegisterUserDTO;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.service.UserService;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.vo.BaseResponse;
import com.vv.common.model.vo.ResponseCode;
import com.vv.common.model.vo.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户通过账号密码登录
     * @return
     */
    @PostMapping("/loginByEmail")
    public BaseResponse loginByEmail(@RequestBody LoginByEmailDTO loginByEmailDTO, HttpServletResponse response){
        if(loginByEmailDTO == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"请求参数错误");
        }
        SafeUserDTO safeUserDTO = userService.loginByEmail(loginByEmailDTO,response);
        return ResultUtils.success(safeUserDTO);
    }

    /**
     * 用户通过手机+验证码登录
     */
    @PostMapping("/loginByPhone")
    public BaseResponse loginByPhone(@RequestBody LoginByPhoneDTO loginByPhoneDTO, HttpServletResponse response){
        if(loginByPhoneDTO == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"请求参数错误");
        }
        SafeUserDTO safeUserDTO = userService.loginByPhone(loginByPhoneDTO,response);
        return ResultUtils.success(safeUserDTO);
    }
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterUserDTO registerUserDTO){
        if(registerUserDTO == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"请求参数错误");
        }
        long userId = userService.userRegister(registerUserDTO);
        if(userId == 0){
            return ResultUtils.error(ResponseCode.REGISTER_ERROR,"注册失败");
        }
        return ResultUtils.success(ResponseCode.SUCCESS,"注册成功");
    }
    /**
     * 用户注册时发送一条短信
     */
    @GetMapping("/registerSms")
    public BaseResponse registerSendCode(@RequestParam String phone){
        if(phone == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"请求参数错误");
        }

        boolean b = userService.registerSms(phone);
        if(!b){
            return ResultUtils.error(ResponseCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS,"发送成功");
    }
    /**
     * 用户登录时发送一条短信
     */
    @GetMapping("/loginSms")
    public BaseResponse loginSendCode(@RequestParam String phone){
        if(phone == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR,"请求参数错误");
        }
        boolean b = userService.loginSms(phone);
        if(!b){
            return ResultUtils.error(ResponseCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS,"发送成功");
    }

}
