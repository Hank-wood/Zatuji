package com.joe.zatuji.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.TUtil;
import com.joe.zatuji.view.LoadingDialog;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Joe on 2016/4/16.
 */
public abstract class BaseFragment<T extends BasePresenter> extends android.support.v4.app.Fragment implements LoadingView {
    protected Activity mActivity;
    protected View mRootView;
    protected AlertDialog dialog;
    protected MyApplication myApplication;
    protected LoadingDialog mLoadingDialog;
    protected T mPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity=getActivity();
        this.myApplication= (MyApplication) mActivity.getApplication();
        //initLeakCanary();
        onSaveFragmentInstance(savedInstanceState);
        initLoading();
    }
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private void onSaveFragmentInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
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
        initPM();

        initView();
        initPresenter();
        initListener();
        return mRootView;
    }

    protected void initPM() {
        mPresenter = TUtil.getT(this,0);
        if(mPresenter!=null) mPresenter.onStart();
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
        if(mLoadingDialog!=null){
            mLoadingDialog.dismiss();
        }
//        dialog.dismiss();
    }

    @Override
    public void showError(String str) {
        KToast.show(str);
    }

    protected void showEmptyView(){
    }

    public void showLoading(String msg){
        if(mLoadingDialog==null) mLoadingDialog = new LoadingDialog(mActivity,msg);
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null) mPresenter.onRemove();
    }

    protected View findView(int id){
        return mRootView.findViewById(id);
    }
}
