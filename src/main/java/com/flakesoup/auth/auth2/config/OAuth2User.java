package com.flakesoup.auth.auth2.config;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OAuth2User implements UserDetails {
    private Long userid;
    private String username;
    private String password;
    private String userext;
    private String clientid;
    private String mobile;
    private List<SimpleGrantedAuthority> authorities;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isEnabled = true;
    private boolean isCredentialsNonExpired = true;

    public OAuth2User() {}

    public OAuth2User(String clientid, String mobile, String username, String password, String ... roles) {
        this(clientid, username, mobile, password, "", 0L, roles);
    }

    public OAuth2User(String clientid, String mobile, String username, String password, String userext, Long userid) {
        this(clientid, username, mobile, password, userext, userid, "");
    }

    public OAuth2User(String clientid, String mobile, String username, String password, String userext, Long userid, String ... roles) {
        this.clientid = clientid;
        this.mobile = mobile;
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.userext = userext;
        this.authorities= Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
