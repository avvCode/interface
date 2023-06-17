package com.vv.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.po.UserInterface;
import com.vv.api.model.vo.UserInterfaceVo;
import com.vv.common.model.vo.IdRequest;

import java.util.List;

/**
* @author vv
*/
public interface UserInterfaceService extends IService<UserInterface> {

    List<UserInterfaceVo> getMyInterface(IdRequest idRequest);
}
