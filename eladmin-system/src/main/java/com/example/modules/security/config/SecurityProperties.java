package com.example.modules.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/26
 * Time: 9:35
 * Description: jwt 的参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class SecurityProperties {

    // request headers
    private String header;

    // 令牌前缀，最后留个空格Bearer
    private String tokenStartWith;

    // 必须使用最少88位的base64对该令牌进行解码
    private String base64Secret;

    // 令牌过期时间,单位毫秒
    private long tokenValidityInSeconds;

    // 在线用户key,根据key在redis查找用户
    private String onlineKey;

    // 验证码key
    private String keyCode;

    public String getTokenStartWitch(){
        return tokenStartWith+" ";
    }



}
