package com.joe.huaban.Home.view;

import com.joe.huaban.Home.model.HomeData;

/**
 * view接口，由activity或者fragment实现
 * Created by Joe on 2016/4/13.
 */
public interface HomeView {
    void showLoading();
    void stopLoading();
    void refreshData(HomeData data);
    void loadMore(HomeData data);
}
