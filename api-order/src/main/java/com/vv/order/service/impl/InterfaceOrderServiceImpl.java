package com.vv.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.order.mapper.InterfaceOrderMapper;
import com.vv.order.model.po.InterfaceOrder;
import com.vv.order.service.InterfaceOrderService;
import org.springframework.stereotype.Service;

/**
* @author zyz19
* @description 针对表【interface_order】的数据库操作Service实现
* @createDate 2023-06-22 21:12:32
*/
@Service
public class InterfaceOrderServiceImpl extends ServiceImpl<InterfaceOrderMapper, InterfaceOrder>
    implements InterfaceOrderService {

}




