package com.flakesoup.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flakesoup.auth.entity.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 */
public interface UserMapper extends BaseMapper<User> {
	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return user
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
