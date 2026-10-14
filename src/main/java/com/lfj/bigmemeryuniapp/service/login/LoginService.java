package com.lfj.bigmemeryuniapp.service.login;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.bigmemeryuniapp.model.dto.UserDto;
import com.lfj.bigmemeryuniapp.model.po.UserPo;
import com.lfj.bigmemeryuniapp.model.vo.UserRegisterFromVo;
import com.lfj.bigmemeryuniapp.model.vo.UserVo;
import com.lfj.bigmemeryuniapp.utils.Result;

public interface LoginService extends IService<UserPo> {
    Result doLogin(UserVo userVo);

    Result logout(String uuid);
    Result userRegister(UserRegisterFromVo userRegisterFromVo);
    Result getUserItem(String userid);
}
