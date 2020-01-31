package com.example.repository;

import com.example.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 18:17
 * Description: No Description
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Long>, JpaSpecificationExecutor<Log> {
}
