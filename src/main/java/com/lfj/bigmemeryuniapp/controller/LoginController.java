package com.lfj.bigmemeryuniapp.controller;

import com.alibaba.fastjson2.JSON;
import com.lfj.bigmemeryuniapp.model.dto.UserDto;
import com.lfj.bigmemeryuniapp.model.po.UserPo;
import com.lfj.bigmemeryuniapp.model.vo.UserRegisterFromVo;
import com.lfj.bigmemeryuniapp.model.vo.UserVo;
import com.lfj.bigmemeryuniapp.service.login.LoginService;
import com.lfj.bigmemeryuniapp.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    Result login(@RequestBody UserPo userPo){
        UserVo userVo = JSON.parseObject(
                JSON.toJSONString(userPo),
                UserVo.class
        );
        return loginService.doLogin(userVo);
    }

    @PostMapping("/register")
    Result Register(@RequestBody UserRegisterFromVo userRegisterFromVo){

        return loginService.userRegister(userRegisterFromVo);
    }

    @GetMapping("/getUserItem")
    Result getUserItem(@RequestParam String userid){
        return loginService.getUserItem(userid);
    }

}
