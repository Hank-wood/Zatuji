package com.joe.zatuji.api.exception;

/**
 * Created by joe on 16/5/29.
 */
public class ResultException  {
    private int code;
    private String error;
    public ResultException(String error){
        this.error = error;
    };
    public ResultException(int code,String error){
        this.code = code;
        this.error = error;
    };
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
