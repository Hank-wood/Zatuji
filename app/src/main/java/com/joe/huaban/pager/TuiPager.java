package com.joe.huaban.pager;

import android.app.Activity;

import com.joe.huaban.global.Constant;

/**
 * Created by Joe on 2016/3/11.
 */
public class TuiPager extends BasePager {
    public TuiPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initUrl() {
        mTitle="美腿";
    }
}
