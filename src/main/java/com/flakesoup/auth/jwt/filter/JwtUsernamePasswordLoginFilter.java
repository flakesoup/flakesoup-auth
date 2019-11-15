package com.flakesoup.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flakesoup.auth.jwt.token.JwtUsernamePasswordLoginToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JwtUsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ThreadLocal<Map<String,String>> threadLocal = new ThreadLocal<>();

    public JwtUsernamePasswordLoginFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                String username = this.obtainUsername(request);
                String password = this.obtainPassword(request);

                // 创建未认证的凭证(etAuthenticated(false)),注意此时凭证中的主体principal为用户名
                JwtUsernamePasswordLoginToken jwtUsernamePasswordLoginToken = new JwtUsernamePasswordLoginToken(username, password);
                // 将认证详情(ip,sessionId)写到凭证
                this.setDetails(request, jwtUsernamePasswordLoginToken);
                // AuthenticationManager获取受支持的AuthenticationProvider(这里也就是JwtAuthenticationProvider),
                // 生成已认证的凭证,此时凭证中的主体为userDetails
                return this.getAuthenticationManager().authenticate(jwtUsernamePasswordLoginToken);
            } catch (Exception e) {
                throw new BadCredentialsException("坏的凭证");
            }
        }
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = this.getBodyParams(request).get(super.SPRING_SECURITY_FORM_PASSWORD_KEY);
        if(!StringUtils.isEmpty(password)){
            return password;
        }
        return super.obtainPassword(request);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = this.getBodyParams(request).get(super.SPRING_SECURITY_FORM_USERNAME_KEY);
        if(!StringUtils.isEmpty(username)){
            return username;
        }
        return super.obtainUsername(request);
    }

    /**
     * 获取body参数 body中的参数只能获取一次
     *
     * @param request
     * @return
     */
    private Map<String, String> getBodyParams(HttpServletRequest request) {
        Map<String, String> bodyParams = threadLocal.get();
        if (bodyParams == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream is = request.getInputStream()) {
                bodyParams = objectMapper.readValue(is, Map.class);
            } catch (IOException e) {
            }
            if (bodyParams == null) bodyParams = new HashMap<>();
            threadLocal.set(bodyParams);
        }
        return bodyParams;
    }
}
