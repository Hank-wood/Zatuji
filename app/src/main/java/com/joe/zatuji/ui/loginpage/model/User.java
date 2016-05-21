package com.joe.zatuji.ui.loginpage.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Joe on 2016/4/30.
 */
public class User extends BmobUser{
    private String nickname;//昵称
    private String avatar;//头像
    private BmobRelation tag;//该用户拥有的图集

    public BmobRelation getTag() {
        return tag;
    }

    public void setTag(BmobRelation tag) {
        this.tag = tag;
    }

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
