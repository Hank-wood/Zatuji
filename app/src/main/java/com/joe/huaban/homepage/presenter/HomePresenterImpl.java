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
    public HomePresenterImpl(HomeView mView) {
        this.mView=mView;
        mKathy=new HomeKathy();
    }

    @Override
    public void getHomeData() {
        mKathy.getHomeData(null,this,false);
    }

    @Override
    public void loadMoreData(String max) {
        mKathy.getHomeData(max,this,true);
    }

    @Override
    public void onSuccess(HomeData data,boolean isLoadMore) {
        if(isLoadMore){
            mView.loadMore(data);
        }else{
            mView.refreshData(data);
        }
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
    }
}
