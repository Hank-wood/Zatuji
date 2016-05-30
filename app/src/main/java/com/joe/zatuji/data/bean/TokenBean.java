package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBmobBean;

/**
 * Created by joe on 16/5/29.
 */
public class TokenBean extends BaseBmobBean{
    public String sessionToken;

    @Override
    public String getTable() {
        return null;
    }
}
