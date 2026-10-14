package com.lfj.bigmemeryuniapp.service.login.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.bigmemeryuniapp.config.JwtProperties;
import com.lfj.bigmemeryuniapp.exception.BadRequestException;
import com.lfj.bigmemeryuniapp.mapper.LoginUserMapper;
import com.lfj.bigmemeryuniapp.model.obj.LoginUser;
import com.lfj.bigmemeryuniapp.model.po.UserPo;
import com.lfj.bigmemeryuniapp.model.vo.UserRegisterFromVo;
import com.lfj.bigmemeryuniapp.model.vo.UserResVo;
import com.lfj.bigmemeryuniapp.model.vo.UserVo;
import com.lfj.bigmemeryuniapp.service.login.LoginService;
import com.lfj.bigmemeryuniapp.utils.BeanUtils;
import com.lfj.bigmemeryuniapp.utils.JwtTool;
import com.lfj.bigmemeryuniapp.utils.RedisCache;
import com.lfj.bigmemeryuniapp.utils.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl extends ServiceImpl<LoginUserMapper, UserPo> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private final JwtProperties jwtProperties;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Override
    public Result doLogin(UserVo userVo) {
        log.info("接收到对象：" + userVo);
        //        //将前端传过来的用户信息放入Token中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userVo.getUsername(), userVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //将前端传过来的用户信息放入Token中
        String username = userVo.getUsername();
        String password = userVo.getPassword();
        UserPo userinfo = this.getOne(new LambdaQueryWrapper<UserPo>().eq(UserPo::getUsername,username));
        if (Objects.isNull(userinfo)) {
            throw new RuntimeException("用户名错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//
//        if (UserStatus.FROZEN.getValue().equals(userinfo.getStatus())) {
//            throw new ForbiddenException("用户被冻结");
//        }
        // 4.校验密码
        if (!passwordEncoder.matches(password, userinfo.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }
        //使用userid生成token
        Long userid = loginUser.getUserPo().getId();
//        Long userid = userinfo.getId();
        String jwt = jwtTool.createToken(userid, jwtProperties.getTokenTTL());
        UserResVo userResVo = new UserResVo();
        userResVo.setUserId(userid);
        userResVo.setToken(jwt);
        userResVo.setUsername(userinfo.getUsername());
        //authenicate存入redis
        redisCache.setCacheObject("login:" + userid ,userResVo, 1, TimeUnit.DAYS);
//        return new Result.success(200,"Success",userLoginVO);
        return  Result.success(userResVo);


    }

    @Override
    public Result logout(String uuid) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUserPo().getId();
//        Long userid = UserContext.getUser();
//        SecurityContextHolder.clearContext();
        redisCache.deleteObject("login:" + userId);
        return  Result.success();
    }

    @Override
    public Result userRegister(UserRegisterFromVo userRegisterFromVo) {
        UserPo selectOne = getBaseMapper().selectOne(new LambdaQueryWrapper<UserPo>().eq(UserPo::getUsername, userRegisterFromVo.getUsername()).or().eq(UserPo::getEmail, userRegisterFromVo.getEmail()).or().eq(UserPo::getPhoneNumber, userRegisterFromVo.getPhoneNumber()));
        if (!Objects.nonNull(selectOne)) {
            UserPo userPo = new UserPo();
            userPo.setUsername(userRegisterFromVo.getUsername());
            userPo.setPassword(passwordEncoder.encode(userRegisterFromVo.getPassword()));
            userPo.setEmail(userRegisterFromVo.getEmail());
            userPo.setPhoneNumber(userRegisterFromVo.getPhoneNumber());
            try {
                save(userPo);
            } catch (Exception e) {
                throw new RuntimeException("添加新用户失败", e);
            }
            return Result.success();
        } else {
            return Result.fail(400,"用户名或邮箱或手机号已存在");
        }


    }

    @Override
    public Result getUserItem(String userid) {
        UserPo userPo;
        try {
            userPo = getById(userid);
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息识别", e);
        }

        return  Result.success(userPo);
    }


}
