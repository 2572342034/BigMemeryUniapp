package com.lfj.bigmemeryuniapp.filter;//package com.lfj.seek_user.filter;

import com.alibaba.fastjson2.JSONObject;
import com.lfj.bigmemeryuniapp.model.obj.LoginUser;
import com.lfj.bigmemeryuniapp.model.po.UserPo;
import com.lfj.bigmemeryuniapp.model.vo.UserResVo;
import com.lfj.bigmemeryuniapp.utils.JwtTool;
import com.lfj.bigmemeryuniapp.utils.RedisCache;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;
import java.util.*;

@Component
@ConditionalOnClass(DispatcherServlet.class)
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter implements HandlerInterceptor {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private final JwtTool jwtTool;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        System.out.println("request：" + );
//        System.out.println("response：" + response);
        String authorization = request.getHeader("Authorization");

        if (!Strings.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);


        Long userid = jwtTool.parseToken(token);
        String loginId = "login:" + userid;
        UserResVo loginUser = redisCache.getCacheObject(loginId);

//        System.out.println("json222 " + redisCache.getCacheObject(loginId).getClass());
//        LoginUser loginUser = new LoginUser();

        ////        这时可能取出来的值为空 所以需要判断一下
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }

        //存入SecurityContextHolder中
        // TODO 获取权限信息封装到Authtication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //放行
        filterChain.doFilter(request, response);
    }


}
