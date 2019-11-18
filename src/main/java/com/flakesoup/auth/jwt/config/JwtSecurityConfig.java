package com.flakesoup.auth.jwt.config;

import com.flakesoup.auth.jwt.config.filter.JwtAuthenticationHeaderFilter;
import com.flakesoup.auth.jwt.config.filter.JwtUsernamePasswordLoginFilter;
import com.flakesoup.auth.jwt.config.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT配置
 */
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置jwt登录验证url
     */
    @Value("${flakesoup.auth.jwtAuthPath:/auth/login}")
    private String jwtAuthPath = "";

    /**
     * 登录过滤器的授权提供者
     */
    @Autowired
    private AuthenticationProvider jwtAuthenticationProvider;

    /**
     * 私钥签名
     */
    @Bean
    public RsaSigner jwtSigner() {
        return new RsaSigner(RsaConfig.PRIVATE_KEY);
    }

    /**
     * 公钥验签(这里是可以通过私钥生成密钥对,包含公钥)
     */
    @Bean
    public RsaVerifier jwtVerifier() {
        return new RsaVerifier(RsaConfig.PUBLIC_KEY);
    }

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder jwtPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * header验证成功
     */
    @Bean
    public JwtHeaderAuthSuccessHandler jwtHeaderAuthSuccessHandler() {
        return new JwtHeaderAuthSuccessHandler();
    }

    /**
      * 登录成功处理
      */
    @Bean
    public AuthenticationSuccessHandler jwtLoginSuccessHandler() {
        return new JwtLoginSuccessHandler();
    }

    /**
     * 登录失败处理
     */
    @Bean
    public AuthenticationFailureHandler jwtLoginFailedHandler() {
        return new JwtLoginFailedHandler();
    }

    /**
     * 未登录或登录过期处理
     */
    @Bean
    public AuthenticationEntryPoint jwtExpiredHandler() {
        return new JwtLoginExpiredHandler();
    }

    /**
     * 权限不通过
     */
    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtPermDeniedHandler();
    }

    /**
     * 用户名密码登录过滤器
     */
    @Bean
    public UsernamePasswordAuthenticationFilter jwtUsernamePasswordLoginFilter() throws Exception {
        UsernamePasswordAuthenticationFilter jwtUsernamePasswordLoginFilter = new JwtUsernamePasswordLoginFilter(jwtAuthPath);
        jwtUsernamePasswordLoginFilter.setAuthenticationManager(this.authenticationManagerBean());
        // 登录成功和失败的操作
        jwtUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler());
        jwtUsernamePasswordLoginFilter.setAuthenticationFailureHandler(jwtLoginFailedHandler());
        return jwtUsernamePasswordLoginFilter;
    }

    /**
     * JWT Header校验过滤器
     */
    @Bean
    OncePerRequestFilter jwtAuthenticationHeaderFilter() {
        JwtAuthenticationHeaderFilter jwtAuthenticationHeaderFilter = new JwtAuthenticationHeaderFilter();
        jwtAuthenticationHeaderFilter.setSuccessHandler(jwtHeaderAuthSuccessHandler());
        return jwtAuthenticationHeaderFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 身份验证入口,当需要登录却没登录时调用
            // 具体为,当抛出AccessDeniedException异常时且当前是匿名用户时调用
            // 匿名用户: 当过滤器链走到匿名过滤器(AnonymousAuthenticationFilter)时,
            // 会进行判断SecurityContext是否有凭证(Authentication),若前面的过滤器都没有提供凭证,
            // 匿名过滤器会给SecurityContext提供一个匿名的凭证(可以理解为用户名和权限为anonymous的Authentication),
            // 这也是JwtHeadFilter发现请求头中没有jwtToken不作处理而直接进入下一个过滤器的原因
            .exceptionHandling().authenticationEntryPoint(jwtExpiredHandler())
            // 拒绝访问处理,当已登录,但权限不足时调用
            // 抛出AccessDeniedException异常时且当不是匿名用户时调用
            .accessDeniedHandler(jwtAccessDeniedHandler())
            .and()
            .authorizeRequests()

            .anyRequest()
            // url动态权限控制
            .access("@accessDecision.hasPermission(request , authentication)").and()
            // 将授权提供者注册到授权管理器中(AuthenticationManager)
            .authenticationProvider(jwtAuthenticationProvider)
            .addFilterAfter(jwtUsernamePasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtAuthenticationHeaderFilter(), JwtUsernamePasswordLoginFilter.class)

            //禁用session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable();
    }

    private class RsaConfig {
        /** RSA私钥 */
        static final String PRIVATE_KEY =
            "-----BEGIN RSA PRIVATE KEY-----\n"
                + "MIICXQIBAAKBgQDibVFda5MKAFkQL07iBlN43GbnJE7yAbc+ShdPRocgKCp3Sxu3\n"
                + "Hc8aebzugDV+Dh6URlYZj5bPyGt2en/eP2YpAW96Kblg5nyxxWmAI6cVWs9FizA8\n"
                + "SRk3h0kThomssBWRUsjzWUAmTYucY/8hjvgxzLPUUX5UtaxCna8I5OHY8wIDAQAB\n"
                + "AoGBAL9xNFErajgTkToY9bYvKRZQK4UU8ta1UqyM0maJuCgdLcKNM5LA1mGJOo/g\n"
                + "wNmisIInchbMi/OEfi+/ZSuRKRq9OPfMcLID3x4/9mRlK31e90YYegiNMKz7RD7T\n"
                + "w0ZsbNemSAwaHDjx+dZFSYCIE9EEyJsKSMePLfmL90gfA7HZAkEA9/k+xYFIbMgA\n"
                + "Kqm3FhxZjzPF3zJlqTGyHBLyIrEUfc2GHcsLa7LT1eARClcGlXDDMHmv74Nkp9mN\n"
                + "93tKt4RlbwJBAOnBiJvPPypcaMKfISBa7X7UNF8LZLUts/+EiCFI8q0hlO3kHq8H\n"
                + "HWkEMmCVR5U9mVx1bVL612bZMYT5yMiNar0CQQCMCBtziyNsErFNZlO2z8GfhZwb\n"
                + "A6m3FxI+mlBUWO16cWJoVq4XXoATyhm1XhmgsHH5YO6Ccg+YXdm2xNAXvFNPAkBn\n"
                + "bB1I8pT75Q7krQs3CYPyjWjudFgGYUY2Uyj3sRLNzwHZjwiUYA1/HUA8w098lFh6\n"
                + "M+o+wIT1GDt0nh9bvFXxAkAg6reUGTclTRXc9JTpXYIltVEne+YKegK6RqeEJUxC\n"
                + "tDB39vwX1Ac/aMtizoeV+RHpuxdKol+7DcNoaHafuGpi\n"
                + "-----END RSA PRIVATE KEY-----";

        /** RSA公钥 */
        static final String PUBLIC_KEY =
            "-----BEGIN PUBLIC KEY-----\n"
                + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDibVFda5MKAFkQL07iBlN43Gbn\n"
                + "JE7yAbc+ShdPRocgKCp3Sxu3Hc8aebzugDV+Dh6URlYZj5bPyGt2en/eP2YpAW96\n"
                + "Kblg5nyxxWmAI6cVWs9FizA8SRk3h0kThomssBWRUsjzWUAmTYucY/8hjvgxzLPU\n"
                + "UX5UtaxCna8I5OHY8wIDAQAB\n"
                + "-----END PUBLIC KEY-----";
    }
}
