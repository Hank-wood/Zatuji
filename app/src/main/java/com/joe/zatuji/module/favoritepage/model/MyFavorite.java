package com.joe.zatuji.module.favoritepage.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 收藏表
 * Created by Joe on 2016/5/1.
 */
public class MyFavorite extends BmobObject{
    private String img_url;//图片地址
    private String desc;//图片描述
    private int width;//
    private int height;//
    private BmobRelation tag;//用户自己添加的标签，多对关系
    private BmobRelation user;//收藏该图片的用户

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BmobRelation getTag() {
        return tag;
    }

    public void setTag(BmobRelation tag) {
        this.tag = tag;
    }

    public BmobRelation getUser() {
        return user;
    }

    public void setUser(BmobRelation user) {
        this.user = user;
    }
}
