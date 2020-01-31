package com.example.service.impl;

import cn.hutool.json.JSONObject;
import com.example.domain.Log;
import com.example.repository.LogRepository;
import com.example.service.LogService;
import com.example.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 18:35
 * Description: No Description
 */
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void save(String userName, String browser, String ip, ProceedingJoinPoint point, Log log) {
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        Method method = methodSignature.getMethod();
        com.example.aop.log.Log annotation = method.getAnnotation(com.example.aop.log.Log.class);

        // 获取方法的路径
        String methodName = point.getTarget().getClass().getName() + "." + methodSignature.getName() + "()";
        StringBuilder params = new StringBuilder("{");
        // 参数值
        Object[] args = point.getArgs();
        // 参数名称
        String[] argNames = methodSignature.getParameterNames();
        if (args !=null){
            for (int i=0;i<args.length;i++){
                params.append(" ").append(argNames[i]).append(": ").append(args[i]);
            }
        }

        // 描述
        if (log != null){
            log.setDescription(annotation.value());
        }

        assert log != null;
        log.setRequestIp(ip);

        String loginPath = "login";
        if (loginPath.equals(methodSignature.getName())){
            try {
                assert args != null;
                userName = new JSONObject(args[0]).get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(userName);
        log.setParams(params.toString()+"}");
        log.setBrowser(browser);
        logRepository.save(log);
    }
}
