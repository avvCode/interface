package com.vv.order.common;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author vv
 * @Description 利用雪花算法生成唯一订单ID,交由IOC维护雪花算法对象单例
 * @date 2023/6/22-21:03
 */
@Component
public class SnowFlakeUtils {
    @Bean
    public Snowflake getSnowflake(){
        return IdUtil.getSnowflake(1, 1);
    }
}
