//package com.lfj.seek_user.filter;
//
//
//import com.Seek.commen.utils.RedisCache;
//import com.alibaba.fastjson.JSON;
//import com.lfj.seek_user.model.Obj.LoginUser;
//import com.lfj.seek_user.model.VO.UserResVo;
//import com.lfj.seek_user.util.JwtTool;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.bitcoinj.core.Base58;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//
//@Component
//@ConditionalOnClass(DispatcherServlet.class)
//@Slf4j
//@RequiredArgsConstructor
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter implements HandlerInterceptor {
//    @Autowired
//    private RedisCache redisCache;
//    @Autowired
//    private final JwtTool jwtTool;
//
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String encodedAuthData = request.getHeader("Sec-WebSocket-Protocol");
//        //获取token
//        String header = request.getHeader("authorization");
//
////        String Sessionid = request.getSession().getId();
//        //这时token可能为空
//        if (!StringUtils.hasText(header) && !StringUtils.hasText(encodedAuthData)){
//            //放行
//            filterChain.doFilter(request,response);
//            return;
//        }
//        String webSocketToken = null;
//        UserResVo websocketVo= null;
//        if (StringUtils.hasText(encodedAuthData)){
//            byte[] decodedBytes = Base58.decode(encodedAuthData);
//            webSocketToken = new String(decodedBytes);
//            websocketVo = JSON.parseObject(webSocketToken, UserResVo.class);
//        }
//
//        UserResVo authorizationVo = JSON.parseObject(header, UserResVo.class);
//
//        //解析token
////        Long userid = parseToken(token);
//        Long userid = jwtTool.parseToken(authorizationVo != null?authorizationVo.getToken(): websocketVo.getToken());
//        String LoginId = authorizationVo !=null? authorizationVo.getLoginId() : websocketVo.getLoginId();
//        //根据解析出来的userid到redis中获取用户信息
//        String redisKey = "login:" + userid + LoginId;
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);
////        这时可能取出来的值为空 所以需要判断一下
//        if(Objects.isNull(loginUser)){
//            throw new RuntimeException("用户未登录");
//        }
//
//
//
//        //存入SecurityContextHolder中
//        // TODO 获取权限信息封装到Authtication中
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//        // 添加 user-info 到请求头中
//        HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
//            @Override
//            public String getHeader(String name) {
//                if ("user-info".equalsIgnoreCase(name)) {
//                    return userid.toString(); // 或者你想传递的其他用户信息
//                }
//                return super.getHeader(name);
//            }
//
//            @Override
//            public Enumeration<String> getHeaders(String name) {
//                List<String> headers = Collections.list(super.getHeaders(name));
//                if ("user-info".equalsIgnoreCase(name)) {
//                    headers.add(userid.toString()); // 或者你想传递的其他用户信息
//                }
//                return Collections.enumeration(headers);
//            }
//
//            @Override
//            public Enumeration<String> getHeaderNames() {
//                List<String> headerNames = Collections.list(super.getHeaderNames());
//                headerNames.add("user-info");
//                return Collections.enumeration(headerNames);
//            }
//        };
//
//
//
//        // 处理 Sec-WebSocket-Protocol 头部
//
//
//        if (StringUtils.hasText(webSocketToken)){
//            response.setHeader("Sec-WebSocket-Protocol", encodedAuthData);
//        }
//
//
//        //放行
//        filterChain.doFilter(wrappedRequest,response);
//    }
//
//
//}
