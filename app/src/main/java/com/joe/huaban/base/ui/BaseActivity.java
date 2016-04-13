package com.joe.huaban.base.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 所有activity的基类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getContent());
        initView();
        initPresenter();
    }

    protected abstract int getContent();
    protected abstract void initView();
    protected  void initPresenter(){
    }
}
