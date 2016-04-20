package com.joe.huaban.homepage.presenter;

import android.content.Context;

import com.joe.huaban.base.LoadingView;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.homepage.model.HomeKathy;
import com.joe.huaban.homepage.view.HomeView;
import com.joe.huaban.global.utils.LogUtils;

/**
 * presenter接口实现类
 * Created by Joe on 2016/4/13.
 */
public class HomePresenterImpl implements HomePresenter,HomeDataListener{
    private LoadingView mLoading;
    private HomeKathy mKathy;
    private HomeView mView;
    private String LastId;//上一次的最后一个pinID
    private boolean isLoadingMore;
    private boolean isGetCache=false;
    public HomePresenterImpl(HomeView mView, LoadingView mLoading, Context context) {
        this.mView=mView;
        this.mLoading=mLoading;
        mKathy=new HomeKathy(context);
        isLoadingMore=false;
    }
    public void getCacheData(){
        mLoading.showLoading();
        if(mKathy.isCacheAvilable()) {
            isGetCache = true;
            mKathy.getPicDataFromCache(null, this, false);
        }else{
            getHomeData();
        }
    }
    @Override
    public void getHomeData() {
        mKathy.getPicDataFromServer(null,this,false);
    }

    @Override
    public void loadMoreData() {
        if(isLoadingMore) return;
        mKathy.getPicDataFromServer(LastId,this,true);
        isLoadingMore=true;

    }

    @Override
    public void onSuccess(PicData data, boolean isLoadMore) {
        if(!isGetCache) mLoading.doneLoading();
        isLoadingMore=false;
        if(isLoadMore){
            mView.loadMore(data);
        }else{
            mView.refreshData(data);
        }
        LastId=data.pins.get(data.pins.size()-1).pin_id;
        isGetCache=false;
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
        mLoading.doneLoading();
        isLoadingMore=false;
        mLoading.showError("嗯，你的网络有问题(⊙﹏⊙)b");
    }
}
