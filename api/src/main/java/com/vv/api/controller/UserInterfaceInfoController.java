package com.vv.api.controller;

import com.vv.api.model.vo.UserInterfaceVo;
import com.vv.api.service.UserInterfaceService;
import com.vv.common.enums.ResponseCode;
import com.vv.common.exception.BusinessException;
import com.vv.common.model.vo.BaseResponse;
import com.vv.common.model.vo.IdRequest;
import com.vv.common.model.vo.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userInterface")
public class UserInterfaceInfoController {

    @Autowired
    private UserInterfaceService userInterfaceService;

    // 查询用户已购买的接口
    @GetMapping("/myInterface")
    BaseResponse getMyInterface(@RequestParam IdRequest idRequest){
        if(idRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        List<UserInterfaceVo> userInterfaceVoList = userInterfaceService.getMyInterface(idRequest);
        if(userInterfaceVoList == null){
            return ResultUtils.error(ResponseCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(userInterfaceVoList);
    }

}
