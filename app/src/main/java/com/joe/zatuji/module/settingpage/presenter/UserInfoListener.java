package com.joe.zatuji.module.settingpage.presenter;

import com.joe.zatuji.module.loginpage.model.User;

/**
 * Created by Joe on 2016/4/30.
 */
public interface UserInfoListener {
    void onUserLogin(User user);
    void onUserNotLogin();
    void onLogOutSuccess();
    void onLogOutError();
}
