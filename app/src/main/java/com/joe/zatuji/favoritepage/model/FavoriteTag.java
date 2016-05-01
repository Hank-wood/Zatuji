package com.joe.zatuji.favoritepage.model;

import com.joe.zatuji.loginpager.model.User;

import cn.bmob.v3.BmobObject;

/**
 * 用户自定义的标签model
 * Created by Joe on 2016/5/1.
 */
public class FavoriteTag extends BmobObject{
    private String tag;//标签名
    private String desc;//标签描述
    private int number;//当前包含了多少张图片
    private boolean is_lock;//是否隐私
    private String pwd;//隐私标签需要密码
    private User belong;//属于哪个用户
    private String front;//封面

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean is_lock() {
        return is_lock;
    }

    public void setIs_lock(boolean is_lock) {
        this.is_lock = is_lock;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User getBelong() {
        return belong;
    }

    public void setBelong(User belong) {
        this.belong = belong;
    }
}
