package com.example.aspect;

import com.example.domain.Log;
import com.example.service.LogService;
import com.example.utils.RequestHolder;
import com.example.utils.SecurityUtils;
import com.example.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.example.utils.SecurityUtils.getUserName;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 18:57
 * Description: No Description
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    private final LogService logService;
    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点*/
    @Pointcut("@annotation(com.example.aop.log.Log)")
    public void logPoinCut(){
        // 该方法没有主体，主要是为了方便别人使用
    }

    /**
     * 配置环绕通知,使用在方法logPointCut()上注册的切入点*/
    @Around("logPoinCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        Log log = new Log("INFO", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest httpServletRequest = RequestHolder.getHttpServletRequest();
        logService.save(getUsername(), StringUtils.getBrowser(httpServletRequest),StringUtils.getIp(httpServletRequest),joinPoint,log);
        return result;
    }

    private String getUsername(){
        try{
            return SecurityUtils.getUserName();
        }catch (Exception e){
            return " ";
        }
    }
}
