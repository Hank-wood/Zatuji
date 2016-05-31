package com.joe.zatuji.base;

import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.utils.TUtil;

/**
 * Created by joe on 16/5/18.
 */
public abstract class BasePresenter<T,E extends BaseModel> {
    protected T mView;
    protected E mModel;
    protected RxJavaManager mRxJavaManager;
    public BasePresenter(){mRxJavaManager = new RxJavaManager();}
    public void setView(T view){
        this.mView = view;
        this.mModel = TUtil.getT(this,1);//获取model实例
    }

    public abstract void onStart();

    public void onRemove(){
        if(mRxJavaManager !=null) mRxJavaManager.remove();
    }
}
