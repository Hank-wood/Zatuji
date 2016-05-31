package com.joe.zatuji.module.homesettingpage;

import com.joe.zatuji.base.view.BaseView;

/**
 * Created by Joe on 2016/4/30.
 */
public interface HomeSettingView extends BaseView{
    void showExit();
    void hideExit();
    void showCache(String cache);
}
