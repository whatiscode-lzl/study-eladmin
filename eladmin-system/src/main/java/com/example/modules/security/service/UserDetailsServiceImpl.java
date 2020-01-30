package com.example.modules.security.service;

import com.example.exception.BadRequestException;
import com.example.modules.security.vo.JwtUser;
import com.example.modules.system.service.UserService;
import com.example.modules.system.service.dto.DeptSmallDto;
import com.example.modules.system.service.dto.JobSmallDto;
import com.example.modules.system.service.dto.UserDto;
import com.example.modules.system.service.mapper.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:10
 * Description: No Description
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("------------------进入UserDetailsServiceImpl下的loadUserByUsername");
        UserDto userDto = userService.finByName(userName);
        if (userDto == null){
            throw new BadRequestException("用户不存在");
        }
        return createUser(userDto);
    }

    private UserDetails createUser(UserDto user) {

        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getSex(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDto::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallDto::getName).orElse(null),
                roleService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}
