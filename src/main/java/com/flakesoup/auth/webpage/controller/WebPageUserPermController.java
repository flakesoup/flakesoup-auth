package com.flakesoup.auth.webpage.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController()
public class WebPageUserPermController {
    /**
     * 普通用户
     * @return
     */
    @Secured("ROLE_USER")
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ADMIN')")
    @RolesAllowed("USER")
    @GetMapping("/perm/normal")
    public Object normalRole(){
        return "User Auth";
    }

    /**
     * Admin用户
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RolesAllowed("ADMIN")
    @GetMapping("/perm/admin")
    public Object adminlRole(){
        return "Admin Auth";
    }
}
