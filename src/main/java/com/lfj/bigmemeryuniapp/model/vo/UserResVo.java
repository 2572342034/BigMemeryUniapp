package com.lfj.bigmemeryuniapp.model.vo;

import lombok.Data;

/**
 * @author LFJ
 * @version 1.0
 * @description: TODO
 * @date 2024/9/20 17:03
 */
@Data
public class UserResVo {
    /**
     * 秘钥token
     */
    private String token;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
}
