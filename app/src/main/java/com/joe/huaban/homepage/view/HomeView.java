package com.joe.huaban.homepage.view;

import com.joe.huaban.homepage.model.HomeData;

/**
 * view接口，由activity或者fragment实现
 * Created by Joe on 2016/4/13.
 */
public interface HomeView {
    void refreshData(HomeData data);
    void loadMore(HomeData data);
}
