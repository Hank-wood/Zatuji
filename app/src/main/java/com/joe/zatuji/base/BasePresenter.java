package com.joe.zatuji.base;

import com.joe.zatuji.utils.TUtil;

/**
 * Created by joe on 16/5/18.
 */
public abstract class BasePresenter<T,E extends BaseModel> {
    protected T mView;
    protected E mModel;
    protected DataManager mDataManager;
    public void setView(T view){
        this.mView = view;
        this.mModel = TUtil.getT(this,1);//获取model实例
        mDataManager = new DataManager();
    }

    public abstract void onStart();

    public void onRemove(){
        if(mDataManager!=null) mDataManager.remove();
    }
}
