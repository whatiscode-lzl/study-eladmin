package com.example.modules.security.security;

import com.example.modules.security.config.SecurityProperties;
import com.example.modules.security.service.OnlineUserService;
import com.example.modules.security.vo.OnlineUser;
import com.example.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/26
 * Time: 12:39
 * Description: No Description
 */
@Slf4j
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public TokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = resolveToken(request);
        String requestRri = request.getRequestURI();

        // 验证token是否存在
        OnlineUser onlineUser = null;
        try{
            SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
            OnlineUserService service = SpringContextHolder.getBean(OnlineUserService.class);
            onlineUser = service.getOne(properties.getOnlineKey()+token);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        if (onlineUser != null && StringUtils.hasText(token) && tokenProvider.validateToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);

        }else {
            log.debug("no valid JWT token found, uri: {}", requestRri);

        }

        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String resolveToken(HttpServletRequest request){
        SecurityProperties   bean = SpringContextHolder.getBean(SecurityProperties.class);

        String header = request.getHeader(bean.getHeader());

        if (StringUtils.hasText(header)&&header.startsWith(bean.getTokenStartWitch())){
            return header.substring(7);
        }
        return null;
    }
}
