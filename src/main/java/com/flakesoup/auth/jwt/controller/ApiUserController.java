package com.flakesoup.auth.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

//@RestController
public class ApiUserController {

    @GetMapping("/user")
    public Object user(Principal princial){
        return princial;
    }

    /**
     * 通过上下文获取当前授权用户
     * @return
     */
    @GetMapping("/user/context")
    public Object userContext(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 通过接口获取当前授权用户
     * @return
     */
    @GetMapping("/user/authenticated")
    public Object userAuthenticated(Authentication authentication){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 通过请求获取当前授权用户
     * @return
     */
    @GetMapping("/user/authenticated_in_request")
    public Object userAuthenticated(HttpServletRequest request){
        return request.getUserPrincipal();
    }
}
