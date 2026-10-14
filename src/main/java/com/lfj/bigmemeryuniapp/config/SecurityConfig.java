package com.lfj.bigmemeryuniapp.config;


import com.lfj.bigmemeryuniapp.filter.JwtAuthenticationTokenFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.security.KeyPair;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 开启方法级权限控制（如 @PreAuthorize）
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig    {

//    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    //    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair(JwtProperties properties){
        // 获取秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        properties.getLocation(),
                        properties.getPassword().toCharArray(),
                        properties.getType()
                );
        //读取钥匙对
        return keyStoreKeyFactory.getKeyPair(
                properties.getAlias(),
                properties.getPassword().toCharArray());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // ← 无状态
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .anyRequest().authenticated()
                )
// 自定义登录页面
               .formLogin(form -> form.disable())   // ✅ 关键
                .httpBasic(basic -> basic.disable()) // ✅ 关键
                .addFilterBefore(
                        jwtAuthenticationTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )


        ;

//        //加入自定义异常
                // 6) 未认证/无权限统一返回 JSON，不再跳转页面
//                .exceptionHandling(eh -> eh
//                        .authenticationEntryPoint(authenticationEntryPoint)
//                        .accessDeniedHandler(accessDeniedHandler)
//                );

        return http.build();
    }


    /**
     * 配置 AuthenticationManager
     */
//    @Bean
//    public AuthenticationManager authenticationManager(
//            HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder builder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        builder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//        return builder.build();
//    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
