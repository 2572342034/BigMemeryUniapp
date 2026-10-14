package com.lfj.bigmemeryuniapp.service.login.Impl;//package com.lfj.seek_user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.lfj.bigmemeryuniapp.mapper.LoginUserMapper;


import com.lfj.bigmemeryuniapp.model.obj.LoginUser;

import com.lfj.bigmemeryuniapp.model.po.UserPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.Objects;

/**
 * @author LFJ
 * @version 1.0
 * @description: TODO
 * @date 2024/7/23 22:30
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginUserMapper loginUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.warn("loadUserByUsername called, username = [{}]", username, new RuntimeException("stacktrace"));

        //根据用户名查询用户信息
//        log.info("userInfo = " + username);
        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPo::getUsername,username).or().eq(UserPo::getEmail,username).or().eq(UserPo::getPhoneNumber,username);
        UserPo user = loginUserMapper.selectOne(wrapper);
//        log.info("user = " + user);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        ArrayList<String> permissionKeyList = new ArrayList<>();
        permissionKeyList.add("system:user");
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        //封装成UserDetails对象返回
        return new LoginUser(user,permissionKeyList);
    }
}