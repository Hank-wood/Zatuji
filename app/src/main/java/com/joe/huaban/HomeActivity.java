package com.joe.huaban;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.joe.huaban.Home.model.HomeData;
import com.joe.huaban.Home.presenter.HomePresenter;
import com.joe.huaban.Home.presenter.HomePresenterImpl;
import com.joe.huaban.Home.view.HomeView;
import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseActivity;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.global.utils.DPUtils;

public class HomeActivity extends BaseActivity implements HomeView{


    private ViewPager mViewPager;
    private PagerSlidingTabStrip mIndicator;
    private HomePresenter mPresenter;

    @Override
    protected int getContent() {return R.layout.activity_home;}
    private void fakeUse() {
        LogUtils.d("开始请求");
        mPresenter.getHomeData(null);
    }
    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenterImpl(this);
        fakeUse();
    }

    @Override
    protected void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_home);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mIndicator.setTextSize(DPUtils.dip2px(this,16f));
        mIndicator.setTextColorResource(android.R.color.white);
    }
    @Override
    public void showLoading() {
        LogUtils.d("loading");
    }

    @Override
    public void stopLoading() {
        LogUtils.d("done");
    }

    @Override
    public void refreshData(HomeData data) {
        LogUtils.d("返回数据"+data.pins.size());
    }

    @Override
    public void loadMore(HomeData data) {
        LogUtils.d("返回数据"+data.pins.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_about){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
