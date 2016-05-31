package com.joe.zatuji.module.loginpage.login;

import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.bean.User;

/**
 * Created by joe on 16/5/31.
 */
public interface LoginView extends BaseView {
    void setAvatar(String url);
    void setUserInfo(User user);
    void resetPwdDone();
}
