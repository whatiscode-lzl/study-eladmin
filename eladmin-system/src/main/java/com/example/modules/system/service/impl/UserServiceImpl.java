package com.example.modules.system.service.impl;

import com.example.exception.EntityNotFoundException;
import com.example.modules.system.domain.User;
import com.example.modules.system.repository.UserRepository;
import com.example.modules.system.service.UserService;
import com.example.modules.system.service.dto.UserDto;
import com.example.modules.system.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:34
 * Description: No Description
 */
@Service
public class UserServiceImpl implements UserService  {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto finByName(String name) {
        User user;
        user = userRepository.findByUsername(name);
        if (null == user){
            throw new EntityNotFoundException(User.class,"name",name);
        }
        return userMapper.toDto(user);
    }
}
