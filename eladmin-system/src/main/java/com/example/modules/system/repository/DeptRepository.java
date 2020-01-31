package com.example.modules.system.repository;

import com.example.modules.system.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 15:15
 * Description: No Description
 */
@SuppressWarnings("all")
public interface DeptRepository extends JpaRepository<Dept,Long>, JpaSpecificationExecutor<Dept> {

    /**
     * 根据角色id查找部门信息*/
    Set<Dept>  findByRoles_id(Long id);
}
