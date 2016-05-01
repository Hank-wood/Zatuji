package com.joe.zatuji.loginpager.presenter;

import com.joe.zatuji.loginpager.model.User;

/**
 * Created by Joe on 2016/5/1.
 */
public interface AvatarListener {
    void onAvatarGet(User user);
    void onAvatarNull();
}
