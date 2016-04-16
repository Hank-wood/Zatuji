package com.joe.huaban.base.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.huaban.base.LoadingView;

/**
 * Created by Joe on 2016/4/16.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment implements LoadingView{
    protected Activity mActivity;
    protected View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity=getActivity();
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

    protected void initListener() {

    }

    protected abstract int getLayout();

    protected void initView(){

    }

    protected abstract void initPresenter();
    @Override
    public void showLoading() {

    }

    @Override
    public void doneLoading() {

    }


}
