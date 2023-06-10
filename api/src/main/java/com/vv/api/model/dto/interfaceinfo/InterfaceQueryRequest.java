package com.vv.api.model.dto.interfaceinfo;


import com.vv.common.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceQueryRequest extends PageRequest implements Serializable {
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
     * 接口状态 0-下线 1-上线
     */
    private Integer status;

    /**
     * 接口计费规则
     */
    private Double price;

    /**
     * 接口库存
     */
    private Integer store;

}
