package com.flakesoup.auth.jwt.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RsaSigner signer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println("Api Login Success Handler!");

        // 生成token返回
        String userJsonStr = JSON.toJSONString(authentication.getPrincipal());
        String token = JwtHelper.encode(userJsonStr, signer).getEncoded();

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write("{\"code\": \"200\", \"msg\": \"登录成功\", \"data\": {\"access_token\": \"" + token + "\"}}");
    }

    public void setSigner(RsaSigner signer) {
        this.signer = signer;
    }
}
