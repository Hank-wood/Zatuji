package com.joe.zatuji.module.homesettingpage.user;

import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.bean.User;

/**
 * Created by joe on 16/5/31.
 */
public interface UserView extends BaseView{
    void setUserInfo(User user);
    void setLoginStyle();
}
