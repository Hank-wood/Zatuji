package com.joe.huaban.pager;

import android.app.Activity;

import com.joe.huaban.utils.ConsUtils;

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
        mUrl= ConsUtils.MEI_TUI;
    }
}
