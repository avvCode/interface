package com.vv.order.model.po;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName interface_order
 */
@TableName(value ="interface_order")
@Data
public class InterfaceOrder implements Serializable {
    /**
     * 订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 接口id
     */
    private Integer interfaceId;

    /**
     * 购买多少次接口
     */
    private Integer buyNumber;

    /**
     * 总计多少钱
     */
    private Integer totalPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 0-未支付 1-支付成功 2-支付失败
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}