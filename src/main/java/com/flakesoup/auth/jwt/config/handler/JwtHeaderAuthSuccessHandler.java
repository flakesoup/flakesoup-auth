package com.flakesoup.auth.jwt.config.handler;

import com.alibaba.fastjson.JSON;
import com.flakesoup.auth.jwt.config.JwtUser;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtHeaderAuthSuccessHandler {
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, JwtUser user) throws IOException {
        System.out.println("Api Header Auth Success Handler!");

        String userJsonStr = JSON.toJSONString(user);

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write("{\"code\": \"200\", \"msg\": \"Header Token验证成功\", \"data\": " + userJsonStr + "}");
    }
}
