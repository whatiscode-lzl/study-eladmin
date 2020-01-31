package com.example.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.example.annotation.AnonymousAccess;
import com.example.aop.log.Log;
import com.example.modules.security.config.SecurityProperties;
import com.example.modules.security.security.TokenProvider;
import com.example.modules.security.service.OnlineUserService;
import com.example.modules.security.vo.AuthUser;
import com.example.modules.security.vo.JwtUser;
import com.example.utils.RedisUtils;
import com.example.utils.SecurityUtils;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/26
 * Time: 19:26
 * Description: 给用户进行授权
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：用户授权接口")
public class AuthController {

    @Value("${loginCode.expiration}")
    private Long expiration;
    @Value("${rsa.private_key}")
    private String privateKey;
    @Value("${single.login:false}")
    private Boolean singleLogin;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public AuthController(SecurityProperties properties, RedisUtils redisUtils, UserDetailsService userDetailsService,
                          OnlineUserService onlineUserService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.properties = properties;
        this.redisUtils = redisUtils;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @AnonymousAccess
    @ApiOperation("获取验证码")
    @GetMapping("/code")
    public ResponseEntity<Object> getCode(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111,36);
        // 设置几位数运算
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        log.info("数算运算的结果:"+result);

        String uuid = properties.getKeyCode() + IdUtil.simpleUUID();
        log.info("uuid=="+uuid);

        // 保存到redis中
        redisUtils.set(uuid,result,expiration, TimeUnit.MINUTES);

        // 验证码信息
        Map<String,Object> imgResult = new HashMap<String,Object>(2){
            {
                put("img",captcha.toBase64());
                put("uuid",uuid);
            }
        };
        return ResponseEntity.ok(imgResult);
    }

    @Log
    @ApiOperation("登入授权")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request){
        // 密码解密
        log.info("-----------authUser:"+authUser.toString());
        RSA rsa = new RSA(privateKey, null);
        //byte[] decrypt = rsa.decrypt(authUser.getPassword(), KeyType.PrivateKey);
        //String password = new String(decrypt);
        String password = "123456";
        log.info("------------------password:"+password);

        // 验证密码和用户
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 交给容器管理
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成token
        String token = tokenProvider.createToken(authentication);
        log.info("------------------token:"+token);
        final JwtUser jwtUser = (JwtUser)authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUser,token,request);

        // 返回token和用户信息
        Map<String,Object> authInfo = new HashMap<String,Object>(){{
            put("token",properties.getTokenStartWith()+token);
            put("jwtUser",jwtUser);
        }};
        if (singleLogin){
            // 踢掉之前登入的
            onlineUserService.checkLoginOnUser(authUser.getUsername(),token);
        }
        return ResponseEntity.ok(authInfo);

    }

    @ApiOperation("获取用户信息")
    @GetMapping(value="/info")
    public ResponseEntity<Object> getUserInfo(){
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(SecurityUtils.getUserName());
        return ResponseEntity.ok(jwtUser);
    }


}
