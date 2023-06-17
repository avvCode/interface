package com.vv.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.mapper.InterfaceMapper;
import com.vv.api.mapper.UserInterfaceMapper;
import com.vv.api.model.po.Interface;
import com.vv.api.model.po.UserInterface;
import com.vv.api.model.vo.UserInterfaceVo;
import com.vv.api.service.UserInterfaceService;
import com.vv.common.model.vo.IdRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author vv
*/
@Service
public class UserInterfaceServiceImpl extends ServiceImpl<UserInterfaceMapper, UserInterface>
    implements UserInterfaceService {

    @Autowired
    private InterfaceMapper interfaceMapper;
    @Override
    public List<UserInterfaceVo> getMyInterface(IdRequest idRequest) {
        //根据用户id查询自己已经购买的接口
        Integer userId  = idRequest.getId();
        //先查询出所有用户id的记录
        QueryWrapper<UserInterface> userInterfaceQueryWrapper = new QueryWrapper<>();
        userInterfaceQueryWrapper.eq("userId",userId);
        List<UserInterface> userInterfaceList = list(userInterfaceQueryWrapper);

        //再查询接口信息
        List<UserInterfaceVo> userInterfaceVoList = userInterfaceList.stream().map(item -> {
            UserInterfaceVo userInterfaceVo = new UserInterfaceVo();
            Integer interfaceId = item.getInterfaceId();
            Interface anInterface = interfaceMapper.selectById(interfaceId);
            BeanUtils.copyProperties(anInterface, userInterfaceVo);
            BeanUtils.copyProperties(item, userInterfaceVo);
            return userInterfaceVo;
        }).collect(Collectors.toList());

        return userInterfaceVoList;
    }
}




