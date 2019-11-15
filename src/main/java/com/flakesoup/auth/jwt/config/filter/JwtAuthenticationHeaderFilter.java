package com.flakesoup.auth.jwt.config.filter;

import com.alibaba.fastjson.JSON;
import com.flakesoup.auth.jwt.config.JwtUser;
import com.flakesoup.auth.jwt.config.JwtUsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截请求进行header token验证
 */
public class JwtAuthenticationHeaderFilter extends OncePerRequestFilter {

    @Autowired
    private RsaVerifier jwtVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token==null || token.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }

        JwtUser user;
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token, jwtVerifier);
            String claims = jwt.getClaims();
            user = JSON.parseObject(claims, JwtUser.class);
            //todo: 可以在这里添加检查用户是否过期,冻结...
        }catch (Exception e){
            //这里也可以filterChain.doFilter(request,response)然后return,那最后就会调用
            //.exceptionHandling().authenticationEntryPoint,也就是本列中的"需要登陆"
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("token 失效");
            return;
        }
        JwtUsernamePasswordAuthenticationToken jwtUsernamePasswordAuthenticationToken = new JwtUsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        jwtUsernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(jwtUsernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }
}
