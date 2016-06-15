package com.joe.zatuji.module.gallerypage;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.Constant;

/**
 * 图集详情页
 * Created by Joe on 2016/5/2.
 */
public class GalleryActivity extends BaseActivity{

    private ActionBar mActionBar;
    private GalleryFragment mFragment;

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
        FavoriteTag tag = (FavoriteTag) getIntent().getSerializableExtra(Constant.GALLERY_TAG);
        mFragment.setTag(tag);
        mActionBar.setTitle(tag.tag+" ("+tag.number+")");
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_container_gallery, mFragment).commit();
    }

    @Override
    protected void initPresenter() {

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
}