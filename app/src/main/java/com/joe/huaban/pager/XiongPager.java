package com.joe.huaban.pager;

import android.app.Activity;

import com.joe.huaban.utils.ConsUtils;

/**
 * Created by Joe on 2016/3/11.
 */
public class XiongPager extends BasePager {
    public XiongPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initUrl() {
        mTitle="大胸";
        mUrl= ConsUtils.DA_XIONG;
    }
}
