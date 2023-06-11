package com.vv.api.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable, UserDetails {
    /**
     * 主键-用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 用户邮箱/账户
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回用户权限
        List<String> permissions = new ArrayList<>();
        String temp = "ROLE_" + userRole;
        permissions.add(temp);
        List<SimpleGrantedAuthority> authorities =
                permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}