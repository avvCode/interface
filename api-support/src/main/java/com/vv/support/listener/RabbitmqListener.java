package com.vv.support.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.vv.common.constant.RabbitmqConstant;
import com.vv.common.constant.RedisConstant;
import com.vv.common.model.to.SmsTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Slf4j
@Component
public class RabbitmqListener {

    @Autowired
    private RedisTemplate redisTemplate;

    //监听sms队列
    @RabbitListener(queues = {RabbitmqConstant.SMS_QUEUE_NAME})
    public void receiveSms(SmsTo smsTo,Message message, Channel channel){

        String code = smsTo.getCode();
        String phone = smsTo.getPhone();

        //TODO 发送短信
        log.info("发送短信到手机：{},验证码为：{}",phone,code);
        try {
            //手动确认消息
            //塞到Redis里
            redisTemplate.opsForValue().set(RedisConstant.REGISTER_CODE_PREFIX+phone,code);

            //手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
