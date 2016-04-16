package com.joe.huaban.homepage.presenter;

import com.joe.huaban.homepage.model.HomeData;

/**
 * 主页数据请求情况的监听类
 * Created by Joe on 2016/4/13.
 */
public interface HomeDataListener {
    void onSuccess(HomeData result,boolean isLoadMore);
    void onError(Throwable ex, boolean isOnCallback);
}
