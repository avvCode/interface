package com.vv.common.constant;

/**
 * 存放消息队列的常量
 */
public class RabbitmqConstant {
    /**
     * 短信交换机名称
     */
    public static final String SMS_EXCHANGE_NAME = "SmsExchange";
    /**
     * 短信队列名称
     */
    public static final String SMS_QUEUE_NAME = "SmsQueue";

    /**
     * 短信routingKey
     */
    public static final String SMS_ROUTING_KET_FOR_REGISTER = "sms.register";

}
