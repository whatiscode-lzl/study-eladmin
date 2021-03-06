package com.example.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:26
 * Description: No Description
 */
@Data
public class UserDto {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String nickName;

    private String sex;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Date lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDto> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDto job;

    private DeptSmallDto dept;

    private Long deptId;

    private Timestamp createTime;
}
