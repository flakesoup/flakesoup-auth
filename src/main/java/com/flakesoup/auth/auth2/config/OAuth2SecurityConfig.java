package com.flakesoup.auth.auth2.config;

import com.flakesoup.auth.auth2.config.exception.OAuth2WebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2SecurityConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private OAuth2WebResponseExceptionTranslator oAuth2WebResponseExceptionTranslator;

    @Autowired
    private TokenStore tokenStore;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
//            .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("flakesoup-gateway")
            .secret(passwordEncoder.encode("flakesoup-gateway"))
            .resourceIds("flakesoup-gateway")
            .authorizedGrantTypes("password", "refresh_token")
            .scopes("all")
            .accessTokenValiditySeconds(300)
            .refreshTokenValiditySeconds(3600)
            .authorities("ROLE_GATEWAY")
            .autoApprove();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore)
            .tokenEnhancer(tokenEnhancer)
//            .userDetailsService(userDetailsService)
            .reuseRefreshTokens(false)
            .exceptionTranslator(oAuth2WebResponseExceptionTranslator);
    }

}
