package com.flakesoup.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	public enum UserStatusEnum {

		USER_STATUS_NORMAL(0, "用户正常"),
		USER_STATUS_EXPIRED(1, "用户过期"),
		USER_STATUS_BLOCK(2, "用户停用");

		private Integer code;

		private String name;

		UserStatusEnum(Integer code, String name) {
			this.code = code;
			this.name = name;
		}

		public Integer getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

	}
}
