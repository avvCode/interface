package com.vv.api.config;

import com.vv.api.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author vv
 * @Description Spring MVC Config
 * @date 2023/6/11-17:52
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 放行接口
     */
    private final List<String> pathPatterns = Arrays.asList(
            "/user/register",
            "/user/registerSms",
            "/user/loginByPhone",
            "/user/loginSms",
            "/user/loginByEmail",
            "/user/logout"
    );

    /**
     * 放行静态资源
     */
    private final List<String> staticPath = Arrays.asList(
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            "/doc.html"
    );

    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(pathPatterns)
                .excludePathPatterns(staticPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 是否发送Cookie
                .allowCredentials(true)
                // 放行哪些原始域
                .allowedOrigins("http://localhost:8000")
                // 放行哪些请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 放行哪些原始请求头部信息
                .allowedHeaders("*")
                // 暴露哪些头部信息
                .exposedHeaders("*");
    }
}
