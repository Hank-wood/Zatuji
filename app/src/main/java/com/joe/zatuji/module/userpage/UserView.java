package com.joe.zatuji.module.userpage;

import com.joe.zatuji.base.view.BaseView;

/**
 * Created by joe on 16/6/2.
 */
public interface UserView extends BaseView {
    void updateAvatar(String url);
    void updateNickName(String nickname);
    void updateEmail(String email);

}
