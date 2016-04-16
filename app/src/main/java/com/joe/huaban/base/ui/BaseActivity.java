package com.joe.huaban.base.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;
import com.joe.huaban.R;
import com.joe.huaban.base.LoadingView;
import com.joe.huaban.global.utils.LogUtils;

/**
 * 所有activity的基类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadingView{
    protected Activity mActivity;
    private AlertDialog dialog;
    private WindowManager windowManager;
    private View loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContent());
        this.mActivity=this;
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary));
        initView();
        initLoading();
        initPresenter();
        initListener();
    }

    protected void initLoading() {
        dialog=new AlertDialog.Builder(this).create();
        loadingView = View.inflate(this, R.layout.dialog_loading,null);
    }

    protected  void initListener(){

    }

    protected abstract int getContent();
    protected abstract void initView();
    protected  void initPresenter(){
    }

    @Override
    public void showLoading(){

        dialog.setView(loadingView);
//        dialog.setContentView(loadingView);
        dialog.show();

        LogUtils.d("loading");
    }
    @Override
    public void doneLoading(){
        dialog.dismiss();
        LogUtils.d("doneLoading");
    }
}
