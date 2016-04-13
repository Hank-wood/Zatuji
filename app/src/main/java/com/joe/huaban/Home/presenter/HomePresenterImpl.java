package com.joe.huaban.Home.presenter;

import com.joe.huaban.Home.model.HomeData;
import com.joe.huaban.Home.model.Homekathy;
import com.joe.huaban.Home.view.HomeView;
import com.joe.huaban.beauty.model.BeautyRequest;
import com.joe.huaban.global.utils.LogUtils;

/**
 * Created by Joe on 2016/4/13.
 */
public class HomePresenterImpl implements HomePresenter,HomeDataListener{
    private Homekathy mKathy;
    private HomeView mView;
    public HomePresenterImpl(HomeView mView) {
        this.mView=mView;
        mKathy=new Homekathy();
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
