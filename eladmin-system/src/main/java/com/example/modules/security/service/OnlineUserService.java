package com.example.modules.security.service;

import com.example.modules.security.config.SecurityProperties;
import com.example.modules.security.vo.JwtUser;
import com.example.modules.security.vo.OnlineUser;
import com.example.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/26
 * Time: 15:59
 * Description: No Description
 */

@Service
@Slf4j
public class OnlineUserService {

    private final SecurityProperties properties;

    private RedisUtils redisUtils;


    public OnlineUserService(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户的信息*/
    public void save(JwtUser jwtUser, String token, HttpServletRequest request){
        String job = jwtUser.getDept()+"/"+jwtUser.getJob();
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String cityInfo = StringUtils.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try{

            onlineUser = new OnlineUser(jwtUser.getUsername(),jwtUser.getNickName(),job,ip,browser,cityInfo, EncryptUtils.desEncrypt(token),new Date());

        }catch (Exception e){

            e.printStackTrace();
        }
        redisUtils.set(properties.getOnlineKey()+token,onlineUser,properties.getTokenValidityInSeconds()/1000);
    }

    /**
     * 查询全部数据*/

    public Map<String,Object> getAll(String filter, Pageable pageable){
        List<OnlineUser> onlineUsers = getAll(filter);

        Map<String, Object> objectMap = PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), onlineUsers),
                onlineUsers.size()
        );
        return objectMap;
    }

    /**
     * 查询全部数据，不分页*/
    private List<OnlineUser> getAll(String filter) {
        List<String> keys = redisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
            if(StringUtils.isNotBlank(filter)){
                if(onlineUser.toString().contains(filter)){
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 踢出用户*/
    public void kickOut(String key) throws Exception {
        key = properties.getOnlineKey() + EncryptUtils.desDecrypt(key);
        redisUtils.del(key);
    }

    /**
     * 退出登入*/
    public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUser user : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("岗位", user.getJob());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 查询用户*/

    public OnlineUser getOne(String key) {
        return (OnlineUser)redisUtils.get(key);
    }

    /**
     * 检测用户是否之前已经登入，是的话推出*/
    public void checkLoginOnUser(String userName, String igoreToken){
        List<OnlineUser> onlineUsers = getAll(userName);
        if(onlineUsers ==null || onlineUsers.isEmpty()){
            return;
        }
        for(OnlineUser onlineUser:onlineUsers){
            if(onlineUser.getUserName().equals(userName)){
                try {
                    String token =EncryptUtils.desDecrypt(onlineUser.getKey());
                    if(StringUtils.isNotBlank(igoreToken)&&!igoreToken.equals(token)){
                        this.kickOut(onlineUser.getKey());
                    }else if(StringUtils.isBlank(igoreToken)){
                        this.kickOut(onlineUser.getKey());
                    }
                } catch (Exception e) {
                    log.error("checkUser is error",e);
                }
            }
        }
    }


}
