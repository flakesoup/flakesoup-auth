package com.flakesoup.auth.jwt.config;

import com.flakesoup.common.core.util.R;
import com.flakesoup.uc.api.UserCenterApi;
import com.flakesoup.uc.api.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailService implements UserDetailsService {
    @Autowired
    private UserCenterApi userCenterApi;

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
        R<UserDto> resp = userCenterApi.getUserById(30L);
        UserDto userDto = resp.getData();
        JwtUser jwtUser = new JwtUser();
        BeanUtils.copyProperties(userDto, jwtUser);
        System.out.println(jwtUser);
        return jwtUser;

        // 测试代码
//        if ("admin".equals(username)) {
//            return new JwtUser("admin", passwordEncoder.encode("123456"));
//        }
//        if ("user".equals(username)) {
//            return new JwtUser("user", passwordEncoder.encode("123456"));
//        }
//        return null;
    }
}
