package com.flakesoup.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = OAuth2ExceptionSerializer.class)
public class BaseAuthException extends OAuth2Exception {
	@Getter
	private String errorCode;

	public BaseAuthException(String msg) {
		super(msg);
	}

	public BaseAuthException(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
