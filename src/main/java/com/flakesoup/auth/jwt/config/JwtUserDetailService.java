package com.flakesoup.auth.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtUserDetailService() {
    }

    /**
     * 模拟数据库查询
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException 未找到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new JwtUser("admin", passwordEncoder.encode("123456"));
        }
        if ("user".equals(username)) {
            return new JwtUser("user", passwordEncoder.encode("123456"));
        }
        return null;
    }
}
