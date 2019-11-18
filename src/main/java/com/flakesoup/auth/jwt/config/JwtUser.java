package com.flakesoup.auth.jwt.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JwtUser implements UserDetails {
    private Long userid;
    private String username;
    private String password;
    private String userext;
    private List<SimpleGrantedAuthority> authorities;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isEnabled;
    private boolean isCredentialsNonExpired = true;

    public JwtUser() {}

    public JwtUser(String username, String password, String ... roles) {
        this(username, password, "", 0L, roles);
    }

    public JwtUser(String username, String password, String userext, Long userid) {
        this(username, password, userext, userid, "");
    }

    public JwtUser(String username, String password, String userext, Long userid, String ... roles) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.userext = userext;
        this.authorities= Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
