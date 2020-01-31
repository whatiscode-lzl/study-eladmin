package com.example.service;

import com.example.domain.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 18:14
 * Description: No Description
 */
public interface LogService {

    /**
     * 保存日志信息*/
    @Async
    void save(String userName, String browser, String ip, ProceedingJoinPoint point, Log log);
}
