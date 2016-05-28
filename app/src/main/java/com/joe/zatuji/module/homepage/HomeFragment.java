package com.joe.zatuji.module.homepage;


import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;

/**
 * 首页
 * Created by Joe on 2016/4/16.
 */
public class HomeFragment extends BaseStaggeredFragment<HomePresenter> {
    private HomeActivity homeActivity;

    @Override
    protected void initView() {
        super.initView();
        homeActivity = (HomeActivity) mActivity;
        setHomeFabHideEnable(homeActivity,true);
    }

}
