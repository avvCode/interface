package com.vv.api.controller;

import com.vv.api.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/interface")
public class InterfaceController {
    @Resource
    private InterfaceService interfaceService;

    //新增

    //修改

    //删除

    //查找

}
