package com.joe.zatuji.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.BaseModel;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.TUtil;
import com.squareup.leakcanary.RefWatcher;

/**
 * 所有activity的基类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseActivity<T extends BasePresenter,E extends BaseModel> extends AppCompatActivity implements LoadingView {
    protected T mPresenter;
    protected E mModel;
    protected Activity mActivity;
    protected MyApplication mApplication;

    private AlertDialog dialog;
    private View loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        this.mActivity=this;
        this.mApplication= (MyApplication) getApplication();
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary));
        initLeakCanary();
        initPM();//初始化presenter和model
        initPresenter();
        initView();
        initLoading();
        initListener();
    }


    private void initPM() {
        mPresenter = TUtil.getT(this,0);
        mModel = TUtil.getT(this,1);
        if(mPresenter!=null) mPresenter.onStart();
    }
    protected abstract int getLayout();
    protected abstract void initView();
    protected abstract void initPresenter();

    protected void initLeakCanary(){
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }


    protected void initLoading() {
        dialog=new AlertDialog.Builder(this).create();
        loadingView = View.inflate(this, R.layout.dialog_loading,null);
    }

    protected  void initListener(){

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null) mPresenter.onRemove();
    }
}
