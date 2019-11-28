package com.flakesoup.auth.jwt.config;

import com.flakesoup.auth.entity.User;
import com.flakesoup.auth.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//@Component
public class JwtUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

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
        User user = userService.getUserByName(username);
        JwtUser jwtUser = new JwtUser();
        BeanUtils.copyProperties(user, jwtUser);
        jwtUser.setUserext(user.getMobile());
        jwtUser.setUserid(user.getId());
        jwtUser.setAccountNonExpired(
            !User.UserStatusEnum.USER_STATUS_EXPIRED.getCode().equals(user.getStatus()));
        jwtUser.setAccountNonLocked(
            !User.UserStatusEnum.USER_STATUS_BLOCK.getCode().equals(user.getStatus()));
        jwtUser.setEnabled(
            User.UserStatusEnum.USER_STATUS_NORMAL.getCode().equals(user.getStatus()));
        System.out.println(jwtUser);
        return jwtUser;
    }
}
