package com.flakesoup.auth.auth2.config;

import com.flakesoup.auth.auth2.config.exception.OAuth2WebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 认证Bean相关配置
 */
@Configuration
public class OAuth2BeanConfigurer {

	@Bean
	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix("flakesoup-auth:");
		return tokenStore;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	protected UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("admin")).authorities("USER").build());
//		return manager;
//	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new OAuth2TokenEnhancer();
	}

	@Bean
	public OAuth2WebResponseExceptionTranslator oAuth2WebResponseExceptionTranslator() {
		return new OAuth2WebResponseExceptionTranslator();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new OAuth2UserDetailService();
	}

}
