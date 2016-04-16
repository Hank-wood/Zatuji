package com.joe.huaban.homepage.presenter;

import com.joe.huaban.homepage.model.HomeData;
import com.joe.huaban.homepage.model.HomeKathy;
import com.joe.huaban.homepage.view.HomeView;
import com.joe.huaban.global.utils.LogUtils;

/**
 * presenter接口实现类
 * Created by Joe on 2016/4/13.
 */
public class HomePresenterImpl implements HomePresenter,HomeDataListener{
    private HomeKathy mKathy;
    private HomeView mView;
    private String LastId;//上一次的最后一个pinID
    private boolean isLoadingMore;
    public HomePresenterImpl(HomeView mView) {
        this.mView=mView;
        mKathy=new HomeKathy();
        isLoadingMore=false;
    }

    @Override
    public void getHomeData() {
        mKathy.getHomeData(null,this,false);
    }

    @Override
    public void loadMoreData() {
        if(isLoadingMore) return;
        mKathy.getHomeData(LastId,this,true);
        isLoadingMore=true;
    }

    @Override
    public void onSuccess(HomeData data,boolean isLoadMore) {
        isLoadingMore=false;
        if(isLoadMore){
            mView.loadMore(data);
        }else{
            mView.refreshData(data);
        }
        LastId=data.pins.get(data.pins.size()-1).pin_id;
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
        isLoadingMore=false;
    }
}
