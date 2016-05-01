package com.joe.zatuji.loginpager.presenter;

/**
 * Created by Joe on 2016/5/1.
 */
public interface LoginListener {
    void loginSuccess();
    void loginError(String msg);
}
