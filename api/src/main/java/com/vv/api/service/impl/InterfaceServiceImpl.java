package com.vv.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.mapper.InterfaceMapper;
import com.vv.api.model.dto.SafeUserDTO;
import com.vv.api.model.dto.interfaceinfo.InterfaceQueryRequest;
import com.vv.api.model.po.Interface;
import com.vv.api.model.vo.InterfaceForUser;
import com.vv.api.model.vo.InterfaceVo;
import com.vv.api.service.InterfaceService;
import com.vv.common.constant.LockConstant;
import com.vv.common.constant.RedisConstant;
import com.vv.common.constant.TokenConstant;
import com.vv.common.enums.InterfaceEnum;
import com.vv.common.enums.ResponseCode;
import com.vv.common.exception.BusinessException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
* @author vv
*/
@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface>
    implements InterfaceService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Override
    public boolean addInterface(InterfaceVo interfaceVo, HttpServletRequest request) {
        //获取用户id
        String token = request.getHeader(TokenConstant.HEAD_AUTHORIZATION);
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
        String token = request.getHeader(TokenConstant.HEAD_AUTHORIZATION);
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
    public Page<InterfaceForUser> getOnlineInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest) {
        return null;
    }

    @Override
    public Page<InterfaceVo> getAllInterfaceByPage(InterfaceQueryRequest interfaceQueryRequest) {
        if (interfaceQueryRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
//        long current = interfaceInfoQueryRequest.getCurrent();
//        Page<InterfaceVo> onlinePage = redisTemplateUtils.getOnlinePage(current);
//        if (onlinePage!=null){
//            //加入缓存后，请求时间由原来的平均68ms ，降低到平局36ms
//            return onlinePage;
//        }
//        long size = interfaceInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        if (size > 20) {
//            throw new BusinessException(ResponseCode.PARAMS_ERROR);
//        }
        // 加分布式锁，同一时刻只能有一个请求数据库，其他的请求循环等待，解决缓存击穿问题.
        for (;;){
            try {
                RLock lock = redissonClient.getLock(LockConstant.INTERFACE_ONLINE_PAGE_LOCK);
                //尝试加锁，最多等待20秒，上锁以后10秒自动解锁
                boolean b = lock.tryLock(20, 10, TimeUnit.SECONDS);
                if (b){
                    //加锁成功,再次检查
//                    Page<InterfaceVo> onlinePageLock = redisTemplateUtils.getOnlinePage(current);
//                    if (onlinePageLock!=null){
//                        lock.unlock();
//                        return onlinePageLock;
//                    }
//                    //仍未命中,查询数据库
//                    Page<InterfaceVo> interfaceInfoVoPage = interfaceInfoMapper.selectOnlinePage(new Page<>(current, size), interfaceInfoQueryRequest);
//                    redisTemplateUtils.onlinePageCache(allInterfaceInfoVoPage);
//                    lock.unlock();
//                    return interfaceInfoVoPage;
                }
                //竞争不到锁，暂时让出CPU资源
                Thread.yield();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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




