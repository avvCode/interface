package com.vv.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.mapper.InterfaceMapper;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.dto.interfaceinfo.InterfaceQueryRequest;
import com.vv.api.model.po.Interface;
import com.vv.api.model.vo.InterfaceVo;
import com.vv.api.service.InterfaceService;
import com.vv.common.constant.CookieConstant;
import com.vv.common.constant.RedisConstant;
import com.vv.common.enums.InterfaceEnum;
import com.vv.common.enums.ResponseCode;
import com.vv.common.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
* @author vv
*/
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface>
    implements InterfaceService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean addInterface(InterfaceVo interfaceVo, HttpServletRequest request) {
        //获取用户id
        String token = request.getHeader(CookieConstant.HEAD_AUTHORIZATION);
        if(token == null){
            return false;
        }
        SafeUserDTO safeUserDTO = (SafeUserDTO) redisTemplate.opsForValue().get(token);
        if (safeUserDTO == null) {
            return false;
        }
        Long userId = safeUserDTO.getId();
        Interface anInterface = new Interface();
        BeanUtils.copyProperties(interfaceVo,anInterface);
        anInterface.setUserId(userId);
        anInterface.setUpdateTime(new Date());
        anInterface.setCreateTime(new Date());
        save(anInterface);
        return true;
    }

    @Override
    public boolean updateInterface(InterfaceVo interfaceVo, HttpServletRequest request) {
        //获取用户id
        String token = request.getHeader(CookieConstant.HEAD_AUTHORIZATION);
        if(token == null){
            return false;
        }
        SafeUserDTO safeUserDTO = (SafeUserDTO) redisTemplate.opsForValue().get(RedisConstant.TOKEN_PREFIX+token);
        if (safeUserDTO == null) {
            return false;
        }
        Long userId = safeUserDTO.getId();
        Interface anInterface = new Interface();
        BeanUtils.copyProperties(interfaceVo,anInterface);
        anInterface.setUserId(userId);
        anInterface.setUpdateTime(new Date());
        updateById(anInterface);
        return true;
    }

    @Override
    public boolean deleteInterface(Long id) {
        if(id == null){
            return false;
        }
        return removeById(id);
    }

    @Override
    public InterfaceVo getInterfaceById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public Page<InterfaceVo> getOnlineInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest) {
        return null;
    }

    @Override
    public Page<InterfaceVo> getAllInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest) {
        return null;
    }

    /**
     * 接口上线
     * @param id
     * @return
     */
    @Override
    public boolean onlineInterface(Long id) {
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
    public boolean offlineInterface(Long id) {
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




