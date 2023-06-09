package com.vv.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.mapper.InterfaceMapper;
import com.vv.api.model.po.Interface;
import com.vv.api.model.vo.InterfaceVo;
import com.vv.api.service.InterfaceService;
import com.vv.common.enums.InterfaceEnum;
import com.vv.common.exception.BusinessException;
import com.vv.common.enums.ResponseCode;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author vv
*/
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface>
    implements InterfaceService {
    @Override
    public boolean addInterface(InterfaceVo interfaceVo, HttpServletRequest request) {
        return false;
    }

    @Override
    public boolean updateInterface(InterfaceVo interfaceVo) {
        return false;
    }

    /**
     * 接口上线
     * @param id
     * @return
     */
    @Override
    public boolean onlineInterface(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Interface anInterface = this.getById(id);
        if (anInterface == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }

        //更新数据库
        Interface interfaceInfo = new Interface();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceEnum.ONLINE.getCode());

        return this.updateById(interfaceInfo);
    }

    /**
     * 接口下线
     * @param id
     * @return
     */
    @Override
    public boolean offlineInterface(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Interface anInterface = this.getById(id);
        if (anInterface == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }

        //更新数据库
        Interface interfaceInfo = new Interface();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceEnum.OFFLINE.getCode());

        return this.updateById(interfaceInfo);
    }
}




