package com.vv.api.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键-用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

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

    /**
     * 是否删除 0-否 1-是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}