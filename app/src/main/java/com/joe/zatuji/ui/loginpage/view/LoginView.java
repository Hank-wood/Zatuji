package com.joe.zatuji.ui.loginpage.view;

import com.joe.zatuji.ui.loginpage.model.User;

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
