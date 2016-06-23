package com.joe.zatuji.module.gallerypage;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joe.zatuji.Event;
import com.joe.zatuji.R;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.Constant;

import rx.functions.Action1;

/**
 * 图集详情页
 * Created by Joe on 2016/5/2.
 */
public class GalleryActivity extends BaseActivity{

    private ActionBar mActionBar;
    private GalleryFragment mFragment;
    private FavoriteTag tag;
    private RxJavaManager mRxManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initFragment();
    }

    private void initFragment() {
        mFragment = new GalleryFragment();
        tag = (FavoriteTag) getIntent().getSerializableExtra(Constant.GALLERY_TAG);
        mFragment.setTag(tag);
        mActionBar.setTitle(tag.tag+" ("+ tag.number+")");
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_container_gallery, mFragment).commit();
    }

    @Override
    protected void initPresenter() {
        mRxManager = new RxJavaManager();
        mRxManager.subscribe(Event.REMOVE_FAVORITE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mActionBar.setTitle(tag.tag+" ("+(tag.number-1)+")");
                tag.number=tag.number-1;
                if(tag.number>=0) mFragment.setTag(tag);
                isRemove = true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    private boolean isRemove =false;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRemove) mRxManager.post(Event.QUITE_GALLERY,null);
        mRxManager.remove();
    }
}