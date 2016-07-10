package com.joe.zatuji.data;

/**
 * Created by joe on 16/5/29.
 */
public class BmobResponseBean {
    private int code;
    private String error;
    public String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
