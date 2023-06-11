package com.vv.api.interceptor;

import com.vv.common.constant.TokenConstant;
import com.vv.common.enums.ResponseCode;
import com.vv.common.exception.BusinessException;
import com.vv.common.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vv
 * @Description 每次请求判断用户是否登录
 * @date 2023/6/11-17:51
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求中的token
        String token = request.getHeader(TokenConstant.HEAD_AUTHORIZATION);
        //验证Token是否合法
        boolean verifyToken = tokenUtils.verifyToken(token);
        if(!verifyToken){
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        //验证Token的payload是否过期
        boolean verifyTime = tokenUtils.verifyTime(token);
        if(!verifyTime){
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        //放行
        return true;
    }
}
