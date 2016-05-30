package com.joe.zatuji.module.loginpage.presenter;

import com.joe.zatuji.data.bean.User;

/**
 * Created by Joe on 2016/5/1.
 */
public interface AvatarListener {
    void onAvatarGet(User user);
    void onAvatarNull();
}
