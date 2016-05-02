package com.joe.zatuji.settingpage.presenter;

import com.joe.zatuji.loginpager.model.User;

/**
 * Created by Joe on 2016/4/30.
 */
public interface UserInfoListener {
    void onUserLogin(User user);
    void onUserNotLogin();
    void onLogOutSuccess();
    void onLogOutError();
}
