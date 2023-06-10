package com.vv.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
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
     * 登录凭证
     */
    private String token;

}
