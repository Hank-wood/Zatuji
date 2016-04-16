package com.joe.huaban;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.joe.huaban.homepage.HomeFragment;
import com.joe.huaban.homepage.model.HomeData;
import com.joe.huaban.homepage.presenter.HomePresenter;
import com.joe.huaban.homepage.presenter.HomePresenterImpl;
import com.joe.huaban.homepage.view.HomeView;
import com.joe.huaban.base.ui.BaseActivity;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.global.utils.DPUtils;

public class HomeActivity extends BaseActivity{

    private static final String TAG_HOME_FRAG = "homeFragment";
    private FragmentManager mFragmentMananger;
    @Override
    protected int getContent() {return R.layout.activity_home;}

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();
    }

    private void initFragment(){

        HomeFragment homeFragment=new HomeFragment();
        mFragmentMananger = getSupportFragmentManager();
        mFragmentMananger.beginTransaction().add(R.id.fl_container_home,homeFragment,TAG_HOME_FRAG).commit();
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
