package com.flakesoup.auth.entity;

public enum UserStatusEnum {

    USER_STATUS_NORMAL(0, "用户正常"),
    USER_STATUS_EXPIRED(1, "用户过期"),
    USER_STATUS_BLOCK(2, "用户停用");

    private int code;

    private String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
