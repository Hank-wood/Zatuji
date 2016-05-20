package com.joe.zatuji.base;

/**
 * Created by joe on 16/5/18.
 */
public abstract class BasePresenter<T,E> {
    protected T mView;
    protected E mModel;
    protected DataManager mDataManager;
    public void setView(T view,E model){
        this.mView = view;
        this.mModel = model;//获取model实例
        mDataManager = new DataManager();
    }

    public abstract void onStart();

    public void onRemove(){
        if(mDataManager!=null) mDataManager.remove();
    }
}
