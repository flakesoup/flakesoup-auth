package com.flakesoup.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flakesoup.auth.entity.User;


public interface UserService extends IService<User> {
	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return 用户信息
	 */
	User getUserById(Long id);

	/**
	 * 通过username查询用户信息
	 *
	 * @param name 用户name
	 * @return user
	 */
	User getUserByName(String name);

	/**
	 * 通过mobile查询用户信息
	 *
	 * @param mobile 用户mobile
	 * @return user
	 */
	User getUserByMobile(String mobile);
}
