package com.joe.zatuji.base;

/**
 * loadingview接口
 * Created by Joe on 2016/4/16.
 */
public interface LoadingView {
    void showLoading();
    void doneLoading();
    void showError(String msg);
}
