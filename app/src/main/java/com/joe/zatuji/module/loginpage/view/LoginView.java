package com.joe.zatuji.module.loginpage.view;

import com.joe.zatuji.module.loginpage.model.User;

/**
 * Created by Joe on 2016/5/1.
 */
public interface LoginView {
    void setAvatar(String url);
    void setUserInfo(User user);
    void doneLogin();
    void showErrorMsg(String msg);
    void resetPwdDone();
    void resetPwdError(String msg);
}
