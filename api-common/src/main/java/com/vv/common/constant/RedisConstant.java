package com.vv.common.constant;

public class RedisConstant {
    /**
     *注册手机验证码业务
     */
    public static final String REGISTER_CODE_PREFIX = "api:register:code:";
    /**
     * 注册手机验证码过期时间 1分钟
     */
    public static final Long REGISTER_CODE_TTL = 60L;
    /**
     *登录手机验证码业务
     */
    public static final String LOGIN_CODE_PREFIX = "api:register:code:";
    /**
     * 登录手机验证码过期时间 1分钟
     */
    public static final Long LOGIN_CODE_TTL = 60L;
    /**
     * Token业务
     */
    public static final String TOKEN_PREFIX = "api:auth:token:";
    /**
     * Token失效 72小时
     */
    public static final Long TOKEN_TTL = 60*60*24*3L;
}
