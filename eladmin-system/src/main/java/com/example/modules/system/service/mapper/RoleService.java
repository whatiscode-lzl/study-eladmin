package com.example.modules.system.service.mapper;

import com.example.modules.system.service.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 15:23
 * Description: No Description
 */
public interface RoleService {

    /**
     * 获取用户的权限*/

    Collection<GrantedAuthority> mapToGrantedAuthorities(UserDto userDto);
}
