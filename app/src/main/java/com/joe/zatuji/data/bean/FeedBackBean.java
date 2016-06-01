package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBmobBean;


/**
 * Created by Joe on 2016/4/30.
 */
public class FeedBackBean extends BaseBmobBean{
    public String type;//意见反馈类型
    public String content;//内容
    public String version;//版本号
    public String android_version;//系统版本号
    public String user_id;
    @Override
    public String getTable() {
        return "FeedBackBean";
    }
}
