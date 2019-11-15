package com.flakesoup.auth.jwt.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtUsernamePasswordLoginToken extends UsernamePasswordAuthenticationToken {
    public JwtUsernamePasswordLoginToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtUsernamePasswordLoginToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
