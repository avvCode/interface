package com.vv.api.common;

import com.vv.common.constant.RabbitmqConstant;
import com.vv.common.model.to.SmsTo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发送短信消息
 */
@Component
@Slf4j
public class RabbitmqUtils implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSms(SmsTo smsTo) {
        //使用rabbitTemplate发送消息
        String message = "send email message to user";
        /**
         * 参数：
         * 1、交换机名称
         * 2、routingKey
         * 3、消息内容
         */
        rabbitTemplate.convertAndSend(RabbitmqConstant.SMS_EXCHANGE_NAME, RabbitmqConstant.SMS_ROUTING_KET_FOR_REGISTER, message);
    }

    /**
     * 1、只要消息抵达服务器，那么b=true
     * @param correlationData 当前消息的唯一关联数据（消息的唯一id）
     * @param b 消息是否成功收到
     * @param s 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }

    /**
     * 设置消息抵达队列的确认
     * 有消息投递到指定的队列失败后，才会触发该回调
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {

    }

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
}
