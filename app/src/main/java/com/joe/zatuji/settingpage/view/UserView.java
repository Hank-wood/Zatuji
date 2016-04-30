package com.joe.zatuji.settingpage.view;

/**
 * Created by Joe on 2016/4/30.
 */
public interface UserView {
    void setAvatar(String url);
    void setDefaultAvatar();
    void setNick(String nick);
    void setLoginOrSignup();
    void showError(String errorMsg);
}
