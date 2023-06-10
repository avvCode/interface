package com.vv.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.dto.interfaceinfo.InterfaceQueryRequest;
import com.vv.api.model.po.Interface;
import com.vv.api.model.vo.InterfaceVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author vv
*/
public interface InterfaceService extends IService<Interface> {
    //新增
    boolean addInterface(InterfaceVo interfaceVo, HttpServletRequest request);
    //修改
    boolean updateInterface(InterfaceVo interfaceVo, HttpServletRequest request);
    //删除
    boolean deleteInterface(Long id);
    //获取一个接口信息
    InterfaceVo getInterfaceById(Long id, HttpServletRequest request);
    //分页获取上线接口
    Page<InterfaceVo> getOnlineInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest);
    //分页获取所有接口
    Page<InterfaceVo> getAllInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest);
    //上线
    boolean onlineInterface(Long id);
    //下线
    boolean offlineInterface(Long id);
}
