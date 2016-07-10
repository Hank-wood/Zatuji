package com.joe.zatuji.module.homepage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.joe.zatuji.Event;
import com.joe.zatuji.R;
import com.joe.zatuji.base.view.HideFabView;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.helper.UpdateHelper;
import com.joe.zatuji.module.discoverpage.DiscoverFragment;
import com.joe.zatuji.module.favoritepage.FavoriteFragment;
import com.joe.zatuji.module.settingpage.SettingActivity;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.module.searchingpage.SearchingActivity;
import com.joe.zatuji.module.homesettingpage.HomeSettingFragment;
import com.joe.zatuji.utils.NetWorkUtils;
import com.joe.zatuji.view.DropMenuDialog;
import com.joe.zatuji.view.MessageDialog;
import com.umeng.analytics.MobclickAgent;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


public class HomeActivity extends BaseActivity implements HideFabView, FloatingToolbar.ItemClickListener, DropMenuDialog.OnMenuClickListener {

    private final String TAG_HOME_FRAG = "homeFragment";
    private final String TAG_DISCOVER_FRAG = "discoverFragment";
    private final String TAG_FAVORITE_FRAG = "favoriteFragment";
    private final String TAG_SETTING_FRAG = "homeSettingFragment";
    private FragmentManager mFragmentManager;
    private HomeFragment homeFragment;
    private String mCurrentFragment;
    private DiscoverFragment discoverFragment;
    private HomeSettingFragment homeSettingFragment;
    private FavoriteFragment favoriteFragment;
    private int mCurrentPos;//当前fragment
    private ActionBar mActionbar;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private FloatingToolbar mBottomBar;
    private DropMenuDialog mTagMenu;
    private UpdateHelper mUpdateHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateHelper = new UpdateHelper(mActivity);
        mUpdateHelper.autoCheckUpdate();
        mFragmentManager = getSupportFragmentManager();
        if(savedInstanceState==null){
            initFragment();
        }else {
            mCurrentFragment = savedInstanceState.getString(CURRENT_FRAG);
            mCurrentPos = savedInstanceState.getInt(CURRENT_POS);
        }

    }


    @Override
    protected int getLayout() {return R.layout.activity_home;}

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mBottomBar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        mActionbar = getSupportActionBar();
        mBottomBar.attachFab(mFab);
        mBottomBar.setClickListener(this);
        findViewById(R.id.home_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SettingActivity.class));
            }
        });
        clearCache();
        checkWifi();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTagMenu = new DropMenuDialog(mActivity);
        mTagMenu.setOnMenuClickListener(this);
    }

    private void clearCache() {
        int size = ImageHelper.getCacheSize();
        if(SettingHelper.getAutoClear() || size>200){
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    ImageHelper.clearCache();
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    private void checkWifi() {
        if(SettingHelper.isNotifyNoWifi()&& NetWorkUtils.getNetType(mActivity)==NetWorkUtils.TYPE_4G){
            MessageDialog dialog = new MessageDialog(mActivity,getResources().getString(R.string.no_wifi_title),getResources().getString(R.string.no_wifi_content));
            dialog.show();
        }
    }


    private void initFragment(){
        homeFragment = new HomeFragment();
        discoverFragment = new DiscoverFragment();
        favoriteFragment = new FavoriteFragment();
        homeSettingFragment = new HomeSettingFragment();
        FragmentTransaction transition= mFragmentManager.beginTransaction().add(R.id.fl_container_home, homeFragment,TAG_HOME_FRAG);
//        transition.addToBackStack(TAG_HOME_FRAG);
        transition.commit();
        mCurrentFragment=TAG_HOME_FRAG;
    }

    //当前的题目
    private void setCurrentTitle(int pos) {
        if(pos==0){
            mActionbar.setTitle("杂图集");
        }else if(pos==2){
            mActionbar.setTitle("收藏");
        }else if(pos==3){
            mActionbar.setTitle("偏好设置");
        }
    }

    private void setCurrentTitle(int pos, String tag) {
            mActionbar.setTitle("发现-"+tag);
    }

    //改变fragment
    private void changeFragment(Fragment switchFragment,String tag) {
        if(!mCurrentFragment.equals(tag)){
            FragmentTransaction transaction= mFragmentManager.beginTransaction();
            if (!switchFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(mFragmentManager.findFragmentByTag(mCurrentFragment)).add(R.id.fl_container_home, switchFragment,tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mFragmentManager.findFragmentByTag(mCurrentFragment)).show(switchFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFragment=tag;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add://新建图集
                favoriteFragment.showCreateTag(null);
                break;
            case R.id.action_change_tag://选择不同标签
                if(mTagMenu==null) mTagMenu = new DropMenuDialog(mActivity);
                mTagMenu.show();
                break;
        }
        return true;
    }
    @Override
    public void onMenuClick(TagBean.Tag tag) {
        setCurrentTitle(1,tag.name);
        discoverFragment.loadAnotherTagData(tag);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.setGroupVisible();
        if(mCurrentPos ==1){
            menu.setGroupVisible(R.id.menu_discover,true);
        }else{
            menu.setGroupVisible(R.id.menu_discover,false);
        }

        if(mCurrentPos ==2){
            menu.setGroupVisible(R.id.menu_favorite,true);
        }else{
            menu.setGroupVisible(R.id.menu_favorite,false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**滑动时隐藏fab*/
    @Override
    public void hideOrShowFAB(boolean hide){
        if(hide){
            if(mBottomBar.isShowing()) mBottomBar.hide();
            if(Build.VERSION.SDK_INT>19) mFab.hide();
//            mFab.hide();
        }else{
            if(mBottomBar.isShowing())return;
            mFab.show();
        }
    }

    @Override
    public void onItemClick(MenuItem menuItem) {
        if(mAppBar.getVisibility()==View.GONE&&menuItem.getItemId()!=R.id.action_setting && menuItem.getItemId()!=R.id.action_search) mAppBar.setVisibility(View.VISIBLE);
        switch (menuItem.getItemId()){
            case R.id.action_home:
                if(homeFragment==null) homeFragment = (HomeFragment) createFragment(TAG_HOME_FRAG);
                changeFragment(homeFragment,TAG_HOME_FRAG);
                setCurrentTitle(0);
                mCurrentPos =0;
                break;
            case R.id.action_discover:
                if(discoverFragment == null) discoverFragment = (DiscoverFragment) createFragment(TAG_DISCOVER_FRAG);
                changeFragment(discoverFragment,TAG_DISCOVER_FRAG);
                setCurrentTitle(1,discoverFragment.getTagName());
                mCurrentPos =1;
                break;
            case R.id.action_favorite:
                if(favoriteFragment == null) favoriteFragment = (FavoriteFragment) createFragment(TAG_FAVORITE_FRAG);
                changeFragment(favoriteFragment,TAG_FAVORITE_FRAG);
                setCurrentTitle(2);
                mCurrentPos =2;
                favoriteFragment.update();
                break;
            case R.id.action_setting:
                if(homeSettingFragment == null) homeSettingFragment = (HomeSettingFragment) createFragment(TAG_SETTING_FRAG);
                changeFragment(homeSettingFragment,TAG_SETTING_FRAG);
                //homeSettingFragment.getCache();
                homeSettingFragment.onResume();
                mAppBar.setVisibility(View.GONE);
                setCurrentTitle(3);
                mCurrentPos =3;

                //mToolbar.setVisibility(View.GONE);
                break;
            case R.id.action_search:
                MobclickAgent.onEvent(mActivity, Event.EVENT_SEARCH);
                Intent i=new Intent(mActivity, SearchingActivity.class);
                startActivity(i);
                break;
        }
        invalidateOptionsMenu();
    }
    public Fragment createFragment(String tag){
        if(mFragmentManager.findFragmentByTag(tag)!=null) return mFragmentManager.findFragmentByTag(tag);
        if(tag.equals(TAG_HOME_FRAG)){
            return new HomeFragment();
        }else if(tag.equals(TAG_DISCOVER_FRAG)){
            return new DiscoverFragment();
        }else if(tag.equals(TAG_FAVORITE_FRAG)){
            return new FavoriteFragment();
        }else if(tag.equals(TAG_SETTING_FRAG)){
            return new HomeSettingFragment();
        }else {
            return null;
        }
    }
    @Override
    public void onItemLongClick(MenuItem menuItem) {
        KToast.show(menuItem.getTitle()+"");
    }

    private long lastTime=0;
    @Override
    public void onBackPressed() {
        long duration = System.currentTimeMillis()-lastTime;
        lastTime = System.currentTimeMillis();
        if(duration<2000)  {
            finish();
            return;
        }
        KToast.show("再按一次退出");
    }

    private static final String CURRENT_FRAG = "current_Tag";
    private static final String CURRENT_POS = "current_Pos";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAG,mCurrentFragment);
        outState.putInt(CURRENT_POS, mCurrentPos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateHelper.remove();
        homeFragment = null;
        discoverFragment = null;
        favoriteFragment = null;
        homeSettingFragment = null;
    }
}
