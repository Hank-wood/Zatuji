package com.joe.huaban.discoverpage.presenter;

import android.content.Context;

import com.joe.huaban.base.LoadingView;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.discoverpage.model.DiscoverKathy;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.homepage.model.HomeKathy;
import com.joe.huaban.homepage.presenter.HomeDataListener;
import com.joe.huaban.homepage.view.HomeView;

/**
 * Created by Joe on 2016/4/18.
 */
public class DiscoverPresenter  implements HomeDataListener{
    private LoadingView mLoading;
    private DiscoverKathy mKathy;
    private HomeView mView;
    private String LastId;//上一次的最后一个pinID
    private boolean isLoadingMore;
    private boolean isGetCache=false;

    public DiscoverPresenter(HomeView mView, LoadingView mLoading, Context context){
        this.mView=mView;
        this.mLoading=mLoading;
        mKathy=new DiscoverKathy(context);
        isLoadingMore=false;
    }

    //初始化数据
    public void getInitData(){
        mLoading.showLoading();
        mKathy.getPicDataFromServer(null,this,false);
    }

    public void getAnotherData(int tag){
        mLoading.showLoading();
        mKathy.setTag(tag);
        mKathy.getPicDataFromServer(null,this,false);
    }

    public void loadMore(){
        if(isLoadingMore) return;
        mKathy.getPicDataFromServer(LastId,this,true);
        isLoadingMore=true;
    }
    @Override
    public void onSuccess(PicData result, boolean isLoadMore) {
        mLoading.doneLoading();
        isLoadingMore=false;
        if(isLoadMore){
            mView.loadMore(result);
        }else{
            mView.refreshData(result);
        }
        LastId=result.pins.get(result.pins.size()-1).pin_id;
        isGetCache=false;
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
        mLoading.doneLoading();
        isLoadingMore=false;
        mLoading.showError();
    }
}
