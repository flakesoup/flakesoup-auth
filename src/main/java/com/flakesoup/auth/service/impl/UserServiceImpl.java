package com.flakesoup.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flakesoup.auth.entity.User;
import com.flakesoup.auth.mapper.UserMapper;
import com.flakesoup.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Override
	public User getUserById(Long id) {
		return baseMapper.getUserById(id);
	}

	@Override
	public User getUserByName(String name) {
		return baseMapper.getUserByName(name);
	}

	@Override
	public User getUserByMobile(String mobile) {
		return baseMapper.getUserByMobile(mobile);
	}

}
