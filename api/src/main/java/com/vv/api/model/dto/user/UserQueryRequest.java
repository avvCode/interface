package com.vv.api.model.dto.user;

import com.vv.common.model.PageRequest;
import com.vv.common.model.vo.IdRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
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
     * token
     */
    private String token;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
