package com.example.modules.system.repository;

import com.example.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:49
 * Description: No Description
 */
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找用户*/
    User findByUsername(String name);
}
