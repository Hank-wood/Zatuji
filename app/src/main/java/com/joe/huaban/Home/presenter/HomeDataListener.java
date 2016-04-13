package com.joe.huaban.Home.presenter;

import com.joe.huaban.Home.model.HomeData;

/**
 * Created by Joe on 2016/4/13.
 */
public interface HomeDataListener {
    void onSuccess(HomeData result);
    void onError(Throwable ex, boolean isOnCallback);
}
