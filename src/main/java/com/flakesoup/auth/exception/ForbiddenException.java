package com.flakesoup.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;


@JsonSerialize(using = OAuth2ExceptionSerializer.class)
public class ForbiddenException extends BaseAuthException {

	public ForbiddenException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}

