package com.closer.xt.sso.model.enums;

public enum LoginType {
    WX(0,"wx");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    LoginType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
