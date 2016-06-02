package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.bean.User;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 用户自定义的标签model
 * Created by Joe on 2016/5/1.
 */
public class FavoriteTag extends BmobObject{
    public String tag;//标签名
    public String desc;//标签描述
    public int number;//当前包含了多少张图片
    public boolean is_lock;//是否隐私
    public String pwd;//隐私标签需要密码
    public Pointer belong;//属于哪个用户
    public Relation img;//该标签拥有的img
    public String front;//封面

}
