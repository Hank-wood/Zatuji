package com.joe.huaban.Home.presenter;

import com.joe.huaban.Home.model.HomeData;
import com.joe.huaban.Home.model.HomeKathy;
import com.joe.huaban.Home.view.HomeView;
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
    public void getHomeData(String max) {
        mView.showLoading();
        mKathy.getHomeData(max,this);

    }
    @Override
    public void onSuccess(HomeData data) {
        mView.stopLoading();
        mView.refreshData(data);
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
        mView.stopLoading();
    }
}
