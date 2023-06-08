package com.vv.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册传回的用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    /**
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 用户验证码
     */
    private String smsCode;
    /**
     * 用户密码
     */
    private String password;
    /**
     *确认密码
      */
    private String confirmPassword;
}
