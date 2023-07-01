package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yongzh
 * @version 1.0
 * @program: hm-dianping
 * @description:
 * @date 2023/5/28 0:10
 */
public class LoginInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要拦截（ThreadLocal中是否有用户）

        UserDTO userDTO= new UserDTO();
        userDTO.setId(123456L);
        userDTO.setNickName("user_7hqvl583dn");

        //5. 存在，保存用户信息到ThreadLocal，UserHolder是提供好了的工具类
        UserHolder.saveUser(userDTO);
        //6. 放行
        return true;
    }


}
