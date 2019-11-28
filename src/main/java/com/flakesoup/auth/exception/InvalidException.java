package com.flakesoup.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = OAuth2ExceptionSerializer.class)
public class InvalidException extends BaseAuthException {

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
