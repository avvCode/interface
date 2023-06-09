package com.vv.api.controller;

import com.vv.api.model.vo.InterfaceVo;
import com.vv.api.service.InterfaceService;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.vo.BaseResponse;
import com.vv.common.model.vo.IdRequest;
import com.vv.common.enums.ResponseCode;
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
    public BaseResponse updateInterface(@RequestBody InterfaceVo interfaceVo){
        if(interfaceVo == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.updateInterface(interfaceVo);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }
    //删除
    @GetMapping("/deleteInterface")
    public BaseResponse deleteInterface(@RequestParam IdRequest idRequest){
        if(idRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.removeById(idRequest);
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }
    //TODO 分页查找

    @GetMapping("/up")
    public BaseResponse upInterface(@RequestParam IdRequest idRequest){
        if(idRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.onlineInterface(idRequest.getId());
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }

    @GetMapping("/down")
    public BaseResponse downInterface(@RequestParam IdRequest idRequest){
        if(idRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = interfaceService.offlineInterface(idRequest.getId());
        if(!b){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return ResultUtils.success(ResponseCode.SUCCESS);
    }

}
