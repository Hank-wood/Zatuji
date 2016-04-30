package com.joe.zatuji.homepage.presenter;

import com.joe.zatuji.base.model.PicData;

/**
 * 主页数据请求情况的监听类
 * Created by Joe on 2016/4/13.
 */
public interface HomeDataListener {
    void onSuccess(PicData result, boolean isLoadMore);
    void onError(Throwable ex, boolean isOnCallback);
}
