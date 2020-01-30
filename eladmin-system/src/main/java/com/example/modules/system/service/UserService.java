package com.example.modules.system.service;

import com.example.modules.system.service.dto.UserDto;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:23
 * Description: 查询用户信息接口
 */
public interface UserService {

    /**
     * 根据名称查找用户*/

    UserDto finByName(String name);
}
