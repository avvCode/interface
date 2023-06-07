package com.vv.api.config;

import com.vv.common.constant.RabbitmqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将队列与交换机绑定
 */
@Configuration
public class RabbitmqConfig {

    //声明sms交换机
    @Bean(RabbitmqConstant.SMS_EXCHANGE_NAME)
    public Exchange SMS_EXCHANGE(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder
                .topicExchange(RabbitmqConstant.SMS_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    //声明sms队列
    @Bean(RabbitmqConstant.SMS_QUEUE_NAME)
    public Queue SMS_QUEUE(){
        return new Queue(RabbitmqConstant.SMS_QUEUE_NAME);
    }


    //sms队列绑定sms交换机，指定routingKey
    @Bean
    public Binding BINDING_ROUTING_KEY_SMS(@Qualifier(RabbitmqConstant.SMS_QUEUE_NAME) Queue queue,
                                          @Qualifier(RabbitmqConstant.SMS_EXCHANGE_NAME) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitmqConstant.SMS_ROUTING_KET_FOR_REGISTER)
                .noargs();
    }
}
