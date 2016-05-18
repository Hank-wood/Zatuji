package com.joe.zatuji.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.global.utils.KToast;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Joe on 2016/4/16.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment implements LoadingView{
    protected Activity mActivity;
    protected View mRootView;
    protected AlertDialog dialog;
    protected MyApplication myApplication;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity=getActivity();
        this.myApplication= (MyApplication) mActivity.getApplication();
        initLeakCanary();
        initLoading();
    }

    protected void initLeakCanary(){
        RefWatcher refWatcher = MyApplication.getRefWatcher(mActivity);
        refWatcher.watch(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(),null);

        initView();
        initPresenter();
        initListener();
        return mRootView;

    }
    protected void initLoading() {
        dialog = new AlertDialog.Builder(mActivity).create();
    }
    protected void initListener() {

    }

    protected abstract int getLayout();

    protected void initView(){

    }

    protected abstract void initPresenter();
    @Override
    public void showLoading() {
        View loadingView=View.inflate(mActivity, R.layout.dialog_loading,null);
        dialog.setContentView(loadingView);
        dialog.show();
    }
    @Override
    public void doneLoading() {
        dialog.dismiss();
    }

    @Override
    public void showError(String str) {
        KToast.show(str);
    }

    protected void showEmptyView(){

    }
}