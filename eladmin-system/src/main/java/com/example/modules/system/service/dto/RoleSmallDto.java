package com.example.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:27
 * Description: No Description
 */
@Data
public class RoleSmallDto implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
