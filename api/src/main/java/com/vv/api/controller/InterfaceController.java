package com.vv.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.api.model.dto.interfaceinfo.InterfaceQueryRequest;
import com.vv.api.model.vo.InterfaceVo;
import com.vv.api.service.InterfaceService;
import com.vv.common.enums.ResponseCode;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.vo.BaseResponse;
import com.vv.common.model.vo.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/interface")
public class InterfaceController {
    @Resource
    private InterfaceService interfaceService;


    //新增
    @PostMapping("/addInterface")
    public BaseResponse addInterface(@RequestBody InterfaceVo interfaceVo, HttpServletRequest request){
        if(interfaceVo == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.addInterface(interfaceVo, request);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }

    //修改
    @PostMapping("/updateInterface")
    public BaseResponse updateInterface(@RequestBody InterfaceVo interfaceVo, HttpServletRequest request){
        if(interfaceVo == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.updateInterface(interfaceVo,request);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }
    //删除
    @GetMapping("/deleteInterface")
    public BaseResponse deleteInterface(@RequestParam long id){
        if(id <= 0){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.removeById(id);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }

    /**
     * 根据 id 获取接口详细信息和用户调用次数
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceVo> getInterfaceInfoById(Long id,HttpServletRequest request) {
        InterfaceVo interfaceById = interfaceService.getInterfaceById(id, request);
        return ResultUtils.success(interfaceById);
    }


    /**
     * 分页获取已开启的列表
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceVo>> listInterfaceInfoByPage(InterfaceQueryRequest interfaceInfoQueryRequest) {

        Page<InterfaceVo> onlineInterfaceByPage = interfaceService.getOnlineInterfaceByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(onlineInterfaceByPage);
    }

    /**
     * 分页获取已所有的列表（包括已下线）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/AllPage")
    public BaseResponse<Page<InterfaceVo>> getAllInterfaceInfoByPage(InterfaceQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        Page<InterfaceVo> allInterfaceByPage = interfaceService.getAllInterfaceByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceByPage);
    }

    @GetMapping("/online")
    public BaseResponse onlineInterface(@RequestParam Long id){
        if(id == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.onlineInterface(id);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }

    @GetMapping("/offline")
    public BaseResponse offlineInterface(@RequestParam Long id){
        if(id == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.offlineInterface(id);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }
}
