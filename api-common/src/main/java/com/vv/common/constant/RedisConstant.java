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
     * 用户信息失效 720小时和Token失效时间一样
     */
    public static final Long USER_INFO_TTL = 60*60*24*3L;
    /**
     * 保存用户信息前缀
     */
    public static final String USER_INFO_PREFIX =  "api:user:info:";

    /**
     * Token业务
     */
    public static final String TOKEN_PREFIX = "api:auth:token:";
    /**
     * Token失效 72小时
     */
    public static final Long TOKEN_TTL = 60*60*24*3L;
}
