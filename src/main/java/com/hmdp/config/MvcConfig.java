package com.hmdp.config;

import com.hmdp.interceptor.LoginInterceptor;
import com.hmdp.interceptor.RefreshTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class MvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册拦截器1");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/code","/blog/hot","/shop-type/**","/shop/**","/upload/**","/voucher/**")
                .order(1);

        log.info("开始注册拦截器0");
        registry.addInterceptor(refreshTokenInterceptor).addPathPatterns("/**").order(0);
    }
}
