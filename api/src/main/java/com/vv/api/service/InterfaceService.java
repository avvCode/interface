package com.vv.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.po.Interface;
import com.vv.api.model.vo.InterfaceVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author vv
*/
public interface InterfaceService extends IService<Interface> {
    boolean addInterface(InterfaceVo interfaceVo, HttpServletRequest request);

    boolean updateInterface(InterfaceVo interfaceVo);

    boolean onlineInterface(Integer id);

    boolean offlineInterface(Integer id);
}
