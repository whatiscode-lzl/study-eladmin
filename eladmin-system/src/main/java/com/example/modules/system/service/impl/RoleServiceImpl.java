package com.example.modules.system.service.impl;

import com.example.modules.system.domain.Menu;
import com.example.modules.system.domain.Role;
import com.example.modules.system.repository.RoleRepository;
import com.example.modules.system.service.dto.UserDto;
import com.example.modules.system.service.mapper.RoleService;
import com.example.utils.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 16:32
 * Description: No Description
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDto userDto) {
        List<Role> roles = roleRepository.findByUsers_Id(userDto.getId());
        Set<String> permissions = roles.stream()
                .filter(role -> StringUtils.isNoneBlank(role.getPermission())).map(Role::getPermission).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(Menu::getPermission).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
