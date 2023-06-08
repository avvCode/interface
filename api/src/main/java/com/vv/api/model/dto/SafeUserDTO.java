package com.vv.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 脱敏后的用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafeUserDTO {
    /**
     * 主键-用户id
     */
    private Integer id;

    /**
     * 性别 男/女
     */
    private String userSex;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户角色 user / admin
     */
    private String userRole;

    /**
     * 密钥key
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 登录凭证
     */
    private String token;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
