package com.example.utils;

import cn.hutool.json.JSONObject;
import com.example.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 14:30
 * Description: 获取当前登入的用户
 */
public class SecurityUtils {

    public static UserDetails getUserDetails(){
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            throw new BadRequestException(HttpStatus.UNAUTHORIZED,"登录过期");
        }
        return  userDetails;
    }

    /**
     * 获取当前用户的名称*/

    public static String getUserName(){
        Object obj = getUserDetails();
        String username = new JSONObject(obj).get("username", String.class);
        return username;
    }
}
