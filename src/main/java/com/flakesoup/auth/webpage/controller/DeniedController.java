package com.flakesoup.auth.webpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeniedController {

    @GetMapping("/denied")
    public String login(){
        return "denied";
    }
}
