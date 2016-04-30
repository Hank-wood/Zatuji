package com.joe.zatuji.homepage.view;

import com.joe.zatuji.base.model.PicData;

/**
 * view接口，由activity或者fragment实现
 * Created by Joe on 2016/4/13.
 */
public interface HomeView {
    void refreshData(PicData data);
    void loadMore(PicData data);
    void stopRefresh();
}
