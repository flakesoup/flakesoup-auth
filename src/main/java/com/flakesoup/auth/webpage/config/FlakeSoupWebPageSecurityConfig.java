package com.flakesoup.auth.webpage.config;

import com.flakesoup.auth.webpage.handler.FormAccessDeniedHandler;
import com.flakesoup.auth.webpage.handler.FormLoginFailedHandler;
import com.flakesoup.auth.webpage.handler.FormLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * web网页权限配置
 */

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class FlakeSoupWebPageSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置表单登录路径是/login，并且不验证
        http.formLogin().loginPage("/login").permitAll();

        // 登出授权
        http.logout().permitAll();

        // 登录失败授权
        http.authorizeRequests().antMatchers("/error").permitAll();

        // 设置成功跳转路径
        // http.formLogin().defaultSuccessUrl("/home");

        // 设置成功/失败跳转路径，在handler中进行成功后处理，比如跳转
        http.formLogin().successHandler(formLoginSuccessHandler()).failureHandler(formLoginFailedHandler());

        // 配置用户权限不足异常
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        // 授权配置
        http.authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                // csrf配置，关闭csrf
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = passwordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
            .withUser("user").password(passwordEncoder.encode("user")).roles("USER")
            .and()
            .withUser("admin").password(passwordEncoder.encode("admin")).roles("ADMIN");
    }

    @Bean
    AuthenticationSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler formLoginFailedHandler() {
        return new FormLoginFailedHandler();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new FormAccessDeniedHandler();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
