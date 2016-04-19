package com.joe.huaban;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.joe.huaban.discoverpage.DiscoverFragment;
import com.joe.huaban.favoritepage.FavoriteFragment;
import com.joe.huaban.global.utils.KToast;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.homepage.HomeFragment;
import com.joe.huaban.base.ui.BaseActivity;
import com.joe.huaban.settingpage.SettingFragment;

public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{

    private final String TAG_HOME_FRAG = "homeFragment";
    private final String TAG_DISCOVER_FRAG = "discoverFragment";
    private final String TAG_FAVORITE_FRAG = "favoriteFragment";
    private final String TAG_SETTING_FRAG = "settingFragment";
    private FragmentManager mFragmentMananger;
    private BottomNavigationBar bottomNavigationBar;
    private HomeFragment homeFragment;
    private Fragment mCurrentFragment;
    private DiscoverFragment discoverFragment;
    private SettingFragment settingFragment;
    private FavoriteFragment favoriteFragment;
    private int currentPos;//当前fragment
    private int tag=1;
    private ActionBar mActionbar;
    private FloatingActionButton mFab;

    @Override
    protected int getContent() {return R.layout.activity_home;}

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_discover, "发现"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_favorite, "收藏"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_setting, "偏好"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        mActionbar = getSupportActionBar();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KToast.show("开发扛不住睡觉了，我还是个摆设");
            }
        });
        initFragment();

    }

    private void initFragment(){
        homeFragment = new HomeFragment();
        discoverFragment = new DiscoverFragment();
        favoriteFragment = new FavoriteFragment();
        settingFragment = new SettingFragment();
        mFragmentMananger = getSupportFragmentManager();
        FragmentTransaction transition=mFragmentMananger.beginTransaction().add(R.id.fl_container_home, homeFragment,TAG_HOME_FRAG);
        transition.addToBackStack(TAG_HOME_FRAG);
        transition.commit();
        mCurrentFragment=homeFragment;
    }

    @Override
    public void onTabSelected(int position) {
        LogUtils.d("onSelected:"+position);
        switch (position){
            case 0:
                changeFragment(homeFragment,TAG_HOME_FRAG);
                break;
            case 1:
                changeFragment(discoverFragment,TAG_DISCOVER_FRAG);
                break;
            case 2:
                changeFragment(favoriteFragment,TAG_FAVORITE_FRAG);
                break;
            case 3:
                changeFragment(settingFragment,TAG_SETTING_FRAG);
                settingFragment.getCache();
                break;
        }
        currentPos=position;
        setCurrentTitle();
        invalidateOptionsMenu();
    }

    //当前的题目
    private void setCurrentTitle() {
        if(currentPos==0){
            mActionbar.setTitle("杂图集");
        }else if(currentPos==2){
            mActionbar.setTitle("收藏");
        }else if(currentPos==3){
            mActionbar.setTitle("偏好设置");
        }else{
            String title="发现-";
            switch (tag){
                case 1:
                    title+="女青年";//妹子
                    break;
                case 2:
                    title+="男青年";//型男
                    break;
                case 3:
                    title+="萌宝";//萌宝
                    break;
                case 4:
                    title+="光影";//摄影
                    break;
                case 5:
                    title+="巧手工";//手工
                    break;
                case 6:
                    title+="衣装";//女装
                    break;
                case 7:
                    title+="在路上";//旅行
                    break;
                case 8:
                    title+="插画";//插画
                    break;
                case 9:
                    title+="建筑";//建筑
                    break;

            }
            mActionbar.setTitle(title);
        }
    }


    //改变fragment
    private void changeFragment(Fragment switchFragment,String tag) {
        if(mCurrentFragment!=switchFragment){
            FragmentTransaction transaction=mFragmentMananger.beginTransaction();
            if (!switchFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(mCurrentFragment).add(R.id.fl_container_home, switchFragment,tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mCurrentFragment).show(switchFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFragment=switchFragment;
        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {
        LogUtils.d("onReselected:"+position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_girl:
                tag =1;
                break;
            case R.id.action_men:
                tag =2;
                break;
            case R.id.action_kid:
                tag =3;
                break;
            case R.id.action_photography:
                tag =4;
                break;
            case R.id.action_diy:
                tag =5;
                break;
            case R.id.action_apparel:
                tag =6;
                break;
            case R.id.action_travel:
                tag =7;
                break;
            case R.id.action_illustration:
                tag =8;
                break;
            case R.id.action_architecture:
                tag =9;
                break;

        }
        setCurrentTitle();
        discoverFragment.loadTagData(tag);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.setGroupVisible();
        if(currentPos==1){
            menu.setGroupVisible(R.id.menu_discover,true);
        }else{
            menu.setGroupVisible(R.id.menu_discover,false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void hideOrShowFAB(boolean hide){
        if(hide){
            mFab.hide();
        }else{
            mFab.show();
        }
    }
}
