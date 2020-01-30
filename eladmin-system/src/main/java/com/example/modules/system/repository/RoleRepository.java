package com.example.modules.system.repository;

import com.example.modules.system.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 16:33
 * Description: No Description
 */
@SuppressWarnings("all")
public interface RoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据用户的id查询用户的角色*/
    List<Role> findByUsers_Id(Long id);
}
