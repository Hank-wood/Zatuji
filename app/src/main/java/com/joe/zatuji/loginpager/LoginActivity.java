package com.joe.zatuji.loginpager;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;

/**
 * Created by Joe on 2016/4/30.
 */
public class LoginActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.BackgroundLogin));
    }

    @Override
    protected int getContent() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }
}
