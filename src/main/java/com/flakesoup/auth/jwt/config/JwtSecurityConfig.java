package com.flakesoup.auth.jwt.config;

import com.flakesoup.auth.jwt.filter.JwtAuthenticationHeadFilter;
import com.flakesoup.auth.jwt.filter.JwtUsernamePasswordLoginFilter;
import com.flakesoup.auth.jwt.handler.ApiLoginExpiredHandler;
import com.flakesoup.auth.jwt.handler.ApiLoginFailedHandler;
import com.flakesoup.auth.jwt.handler.ApiLoginSuccessHandler;
import com.flakesoup.auth.jwt.handler.ApiPermDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * web网页权限配置
 */
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUserDetailServiceImpl jwtUserDetailService;

    @Autowired
    private RsaVerifier verifier;

    @Autowired
    private RsaSigner signer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录过滤器
        JwtUsernamePasswordLoginFilter jwtUsernamePasswordLoginFilter = new JwtUsernamePasswordLoginFilter();
        jwtUsernamePasswordLoginFilter.setAuthenticationManager(this.authenticationManagerBean());

        //登录成功和失败的操作
        ApiLoginSuccessHandler loginSuccessHandler = apiLoginSuccessHandler();
        loginSuccessHandler.setSigner(signer);
        jwtUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        jwtUsernamePasswordLoginFilter.setAuthenticationFailureHandler(apiLoginFailedHandler());

        //登录过滤器的授权提供者(就这么叫吧)
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
        provider.setPasswordEncoder(jwtPasswordEncoder());
        provider.setUserDetailsService(jwtUserDetailService);

        //JWT校验过滤器
        JwtAuthenticationHeadFilter headFilter = new JwtAuthenticationHeadFilter();
        headFilter.setVerifier(verifier);

        http
                // 身份验证入口,当需要登录却没登录时调用
                // 具体为,当抛出AccessDeniedException异常时且当前是匿名用户时调用
                // 匿名用户: 当过滤器链走到匿名过滤器(AnonymousAuthenticationFilter)时,
                // 会进行判断SecurityContext是否有凭证(Authentication),若前面的过滤器都没有提供凭证,
                // 匿名过滤器会给SecurityContext提供一个匿名的凭证(可以理解为用户名和权限为anonymous的Authentication),
                // 这也是JwtHeadFilter发现请求头中没有jwtToken不作处理而直接进入下一个过滤器的原因
                .exceptionHandling().authenticationEntryPoint(apiExpiredHandler())
                // 拒绝访问处理,当已登录,但权限不足时调用
                // 抛出AccessDeniedException异常时且当不是匿名用户时调用
                .accessDeniedHandler(apiAccessDeniedHandler())
                .and()
                .authorizeRequests()

                .anyRequest().access("@accessDecision.hasPermission(request , authentication)")
                .and()

                // 将授权提供者注册到授权管理器中(AuthenticationManager)
                .authenticationProvider(provider)
                .addFilterAfter(jwtUsernamePasswordLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(headFilter, JwtUsernamePasswordLoginFilter.class)

                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }

    @Bean
    ApiLoginSuccessHandler apiLoginSuccessHandler() {
        return new ApiLoginSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler apiLoginFailedHandler() {
        return new ApiLoginFailedHandler();
    }

    @Bean
    AuthenticationEntryPoint apiExpiredHandler() {
        return new ApiLoginExpiredHandler();
    }

    @Bean
    AccessDeniedHandler apiAccessDeniedHandler() {
        return new ApiPermDeniedHandler();
    }

    @Bean
    PasswordEncoder jwtPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
