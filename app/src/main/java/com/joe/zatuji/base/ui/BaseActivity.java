package com.joe.zatuji.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.global.utils.LogUtils;

/**
 * 所有activity的基类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadingView{
    protected Activity mActivity;
    protected MyApplication mApplication;
    private AlertDialog dialog;
    private View loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContent());
        this.mActivity=this;
        this.mApplication= (MyApplication) getApplication();
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
        dialog.show();
        dialog.setContentView(loadingView);

        LogUtils.d("loading");
    }
    @Override
    public void doneLoading(){
        dialog.dismiss();
        LogUtils.d("doneLoading");
    }
    @Override
    public void showError(String str) {
        KToast.show(str);
    }
}
