package com.example.modules.system.service.mapper;

import com.example.base.BaseMapper;
import com.example.modules.system.domain.User;
import com.example.modules.system.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:57
 * Description: No Description
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {

    @Override
    @Mapping(source = "user.userAvatar.realName",target = "avatar")
    UserDto toDto(User user);
}
