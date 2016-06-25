package com.joe.zatuji.view.base;

import android.app.Dialog;
import android.content.Context;

import com.joe.zatuji.R;

/**
 * Created by joe on 16/5/28.
 */
public abstract class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setContentView(getLayout());
        initView();
        initData();
        initListener();
    }
    public BaseDialog(Context context) {
        super(context, R.style.dialog_theme);
        this.mContext = context;
        setContentView(getLayout());
        initView();
        initData();
        initListener();
    }


    protected abstract int getLayout();
    protected abstract void initView();
    private void initData() {

    }
    protected void initListener() {

    }
}
