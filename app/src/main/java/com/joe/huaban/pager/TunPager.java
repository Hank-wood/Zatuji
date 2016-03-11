package com.joe.huaban.pager;

import android.app.Activity;

import com.joe.huaban.utils.ConsUtils;

/**
 * Created by Joe on 2016/3/11.
 */
public class TunPager extends BasePager {
    public TunPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initUrl() {
        mTitle="翘臀";
        mUrl= ConsUtils.QIAO_TUN;
    }
}
