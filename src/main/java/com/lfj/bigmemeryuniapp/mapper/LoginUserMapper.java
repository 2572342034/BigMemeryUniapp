package com.lfj.bigmemeryuniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lfj.bigmemeryuniapp.model.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginUserMapper extends BaseMapper<UserPo> {
}
