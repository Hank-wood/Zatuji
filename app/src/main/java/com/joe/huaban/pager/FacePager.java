package com.joe.huaban.pager;

import android.app.Activity;

import com.joe.huaban.global.Constant;

/**
 * Created by Joe on 2016/3/11.
 */
public class FacePager extends BasePager {
    public FacePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initUrl() {
        mTitle="颜值";
    }
}
