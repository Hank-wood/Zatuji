package com.joe.huaban.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.joe.huaban.Home.model.HomeData;
import com.joe.huaban.Home.presenter.HomePresenter;
import com.joe.huaban.Home.presenter.HomePresenterImpl;
import com.joe.huaban.Home.view.HomeView;
import com.joe.huaban.R;
import com.joe.huaban.beauty.model.BeautyRequest;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.pager.BasePager;
import com.joe.huaban.global.utils.DPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

public class HomeActivity extends AppCompatActivity implements HomeView{


    private List<BasePager> pagers;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mIndicator;
    private HomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initPresenter();
        fakeUse();
//        initAdapter();
    }

    private void fakeUse() {
        LogUtils.d("开始请求");
        mPresenter.getHomeData(null);
    }

    private void initPresenter() {
        mPresenter = new HomePresenterImpl(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_about){
            showAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("声明");
        builder.setMessage("本应用旨在技术交流，请勿乱用");
        builder.show();
    }

    private void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_home);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mIndicator.setTextSize(DPUtils.dip2px(this,16f));
        mIndicator.setTextColorResource(android.R.color.white);
    }
    private void initAdapter() {
        MyAdapter adapter=new MyAdapter();
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //pagers.get(0).initData();
    }

    private void initData() {
        pagers = new ArrayList<BasePager>();
        /*pagers.add(new FacePager(this));
        pagers.add(new XiongPager(this));
        pagers.add(new TunPager(this));
        pagers.add(new TuiPager(this));*/

    }

    private void requestBeauty() {
        new Thread(){
            @Override
            public void run() {
                BeautyRequest.getInstance().getBeautyData();
            }
        }.start();

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

    class MyAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return pagers.get(position).mTitle;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager=pagers.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
