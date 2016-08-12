package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBmobBean;

/**
 * description - 启动页
 * <p/>
 * author - Joe.
 * create on 16/8/11.
 * change
 * change on .
 */


public class WelcomeCover extends BaseBmobBean {
    public String by;
    public String url;
    public String weiBo;
    public String reason;
    public boolean using;

    @Override
    public String toString() {
        return "WelcomeCover{" +
                "by='" + by + '\'' +
                ", url='" + url + '\'' +
                ", weiBo='" + weiBo + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
