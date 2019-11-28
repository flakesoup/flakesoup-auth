package com.flakesoup.auth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * OAuth2 异常格式化
 */
public class OAuth2ExceptionSerializer extends StdSerializer<BaseAuthException> {
	public OAuth2ExceptionSerializer() {
		super(BaseAuthException.class);
	}

	@Override
	@SneakyThrows
	public void serialize(BaseAuthException value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", 1);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}
