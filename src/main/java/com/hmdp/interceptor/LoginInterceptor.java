package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;


@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // 从请求头中获得token
        String token = request.getHeader("authorization");
        // 判断token是否存在
        if(token == null || token.isEmpty()){
            log.info("用户未登录");
            response.setStatus(401);
            return false;
        }
        // 根据token从redis中获取user
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + token);
        //用户不存在
        if(map.isEmpty()){
            log.info("用户未登录");
            response.setStatus(401);
            return false;
        }
        UserDTO user = BeanUtil.fillBeanWithMap(map, new UserDTO(), false);
        //用户存在，保存到ThreadLocal
        UserHolder.saveUser(user);

        //刷新token有效期
        redisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, LOGIN_USER_TTL, java.util.concurrent.TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
