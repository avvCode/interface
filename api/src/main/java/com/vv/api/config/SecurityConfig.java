package com.vv.api.config;

import com.vv.api.common.SimpleAccessDeniedHandler;
import com.vv.api.common.SimpleAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author vv
 * @Description Spring Security配置类
 * @date 2023/6/11-12:59
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SimpleAccessDeniedHandler simpleAccessDeniedHandler;

    @Autowired
    private SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint;

    /**
     * 解加密器
     * @return
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //允许跨域
                .cors()
                .and()
                //关闭csrf
                .csrf().disable()
                .authorizeRequests()
                // 管理员才可访问的接口
                .antMatchers().hasRole("admin")
                // 对于登录接口 允许匿名访问.anonymous()，即未登陆时可以访问，登陆后携带了token就不能再访问了
                .antMatchers(
                        "/user/loginByPhone",
                        "/user/loginByEmail",
                        "/user/register",
                        "/user/registerSms"
                ).anonymous()

                .antMatchers(
                        "/user/checkUserLogin",
                        "/user/loginSms")
                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证,.authenticated()表示认证之后可以访问
                .anyRequest()
                .authenticated();

        //注册自定义异常响应
        http.exceptionHandling()
                .accessDeniedHandler(simpleAccessDeniedHandler)
                .authenticationEntryPoint(simpleAuthenticationEntryPoint);

        //开启配置注销登录功能
        http.logout()
                .logoutUrl("/user/logout");//指定用户注销登录时请求访问的地址
                //.deleteCookies(CookieConstant.headAuthorization)//指定用户注销登录后删除的 Cookie。
                //.deleteCookies(CookieConstant.autoLoginAuthCheck)
                //.logoutSuccessUrl("http://122.9.148.119:88/api/user/logoutSuccess");//指定退出登录后跳转的地址
        //每个浏览器最多同时只能登录1个用户
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
    }
}
