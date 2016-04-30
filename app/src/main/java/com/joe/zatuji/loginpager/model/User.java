package com.joe.zatuji.loginpager.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Joe on 2016/4/30.
 */
public class User extends BmobUser{
    private String nickname;//昵称
    private String avatar;//头像

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
