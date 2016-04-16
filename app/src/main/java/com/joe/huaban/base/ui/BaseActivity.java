package com.joe.huaban.base.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.joe.huaban.base.LoadingView;
import com.joe.huaban.global.utils.LogUtils;

/**
 * 所有activity的基类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadingView{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContent());
        initView();
        initPresenter();
    }

    protected abstract int getContent();
    protected abstract void initView();
    protected  void initPresenter(){
    }

    @Override
    public void showLoading(){
        LogUtils.d("loading");
    }
    @Override
    public void doneLoading(){
        LogUtils.d("doneLoading");
    }
}
