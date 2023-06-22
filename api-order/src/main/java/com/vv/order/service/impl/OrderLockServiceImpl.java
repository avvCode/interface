package com.vv.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.vv.order.mapper.OrderLockMapper;
import com.vv.order.model.po.OrderLock;
import com.vv.order.service.OrderLockService;
import org.springframework.stereotype.Service;

/**
* @author zyz19
* @description 针对表【order_lock】的数据库操作Service实现
* @createDate 2023-06-22 21:12:39
*/
@Service
public class OrderLockServiceImpl extends ServiceImpl<OrderLockMapper, OrderLock>
    implements OrderLockService {

}




