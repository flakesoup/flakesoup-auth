package com.flakesoup.auth.auth2.config;

import com.flakesoup.auth.entity.User;
import com.flakesoup.auth.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    public OAuth2UserDetailService() {
    }

    /**
     * 模拟数据库查询
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException 未找到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.getUserByName(username);
        OAuth2User oAuth2User = new OAuth2User();
//        BeanUtils.copyProperties(user, OAuth2User);
//        OAuth2User.setUserext(user.getMobile());
//        OAuth2User.setUserid(user.getId());
//        OAuth2User.setAccountNonExpired(
//            !User.UserStatusEnum.USER_STATUS_EXPIRED.getCode().equals(user.getStatus()));
//        OAuth2User.setAccountNonLocked(
//            !User.UserStatusEnum.USER_STATUS_BLOCK.getCode().equals(user.getStatus()));
//        OAuth2User.setEnabled(
//            User.UserStatusEnum.USER_STATUS_NORMAL.getCode().equals(user.getStatus()));
//        System.out.println(OAuth2User);
        oAuth2User.setUserid(1L);
        oAuth2User.setUsername(username);
        oAuth2User.setUserext("ext data");
        oAuth2User.setMobile("13466730687");
        oAuth2User.setPassword(new BCryptPasswordEncoder().encode("admin"));
        return oAuth2User;
    }
}
