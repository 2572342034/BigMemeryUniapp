package com.lfj.bigmemeryuniapp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author LFJ
 * @version 1.0
 * @description: 求职者实体类（user） TODO
 * @date 2024/7/23 21:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码
     */

}
