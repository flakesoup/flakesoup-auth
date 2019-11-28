//package com.flakesoup.auth.jwt.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class JwtTestController {
//    /**
//     * 任何人都能访问
//     * @return
//     */
//    @GetMapping("/publicMsg")
//    public String getMsg(){
//        return "you get the message!";
//    }
//
//    /**
//     * 登录的用户才能访问
//     * @return
//     */
//    @GetMapping("/innerMsg")
//    public String innerMsg(){
//        return "you get the message!";
//    }
//
//    /**
//     * 管理员(admin)才能访问
//     * @return
//     */
//    @GetMapping("/secret")
//    public String secret(){
//        return "you get the message!";
//    }
//
//}
