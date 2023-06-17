package com.vv.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vv
 * @Description 用户看到的接口信息
 * @date 2023/6/17-18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceForUser {
    /**
     * 接口id
     */
    private Integer interfaceId;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口计费规则
     */
    private Double price;

    /**
     * 接口发布者
     */
    private Long userId;

    /**
     * 接口库存
     */
    private Integer store;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
