package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBmobBean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Joe on 2016/4/30.
 */
public class User extends BaseBmobBean{
    public String nickname;//昵称
    public String avatar;//头像
//    public BmobRelation tag;//该用户拥有的图集
    public String username;
    public String password;
    public String email;
    public Relation tag;//该用户拥有的图集
    public String cdn;//头像删除时有用
    @Override
    public String toString() {
        return "User{" +
                "sessionToken='" + sessionToken + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", tag=" + tag +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                '}';
    }

    public Boolean emailVerified;
    public String sessionToken;
    public String mobilePhoneNumber;
    public Boolean mobilePhoneNumberVerified;

//    public BmobRelation getTag() {
//        return tag;
//    }
//
//    public void setTag(BmobRelation tag) {
//        this.tag = tag;
//    }

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

    @Override
    public String getTable() {
        return "_User";
    }
}
