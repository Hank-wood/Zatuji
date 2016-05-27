package com.joe.zatuji.module.gallerypage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.module.favoritepage.model.FavoriteTag;
import com.joe.zatuji.module.favoritepage.model.MyFavorite;
import com.joe.zatuji.module.gallerypage.adapter.GalleryAdapter;
import com.joe.zatuji.module.gallerypage.presenter.GalleryPresenter;
import com.joe.zatuji.module.gallerypage.view.GalleryView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;

import java.util.ArrayList;

/**
 * 图集详情页
 * Created by Joe on 2016/5/2.
 */
public class GalleryActivity extends BaseActivity implements GalleryView{

    private ActionBar mActionBar;
    private FavoriteTag mTag;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;
    private GalleryPresenter mPresenter;
    private StaggeredGridLayoutManager mLayoutManager;
    private GalleryAdapter mAdapter;

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
        initTag();
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_gallery);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_gallery);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new GalleryAdapter(mActivity);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GalleryPresenter(mActivity,mTag,this,this);
        mPresenter.getGallery();
    }

    @Override
    protected void initListener() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshGallery();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.d("y"+dy);
                int[] visibleItems = mLayoutManager.findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);

                if (dy > 0 && lastitem > mAdapter.getItemCount() - 5) {
                    mPresenter.loadMoreGallery();
                }
            }
        });
    }

    private void initTag() {
        mTag = (FavoriteTag) getIntent().getSerializableExtra(Constant.GALLERY_TAG);
        mActionBar.setTitle(mTag.getTag());
    }

    @Override
    public void showLoadError(String msg) {
        mRefresh.setRefreshing(false);
        KToast.show(msg);
    }

    @Override
    public void refreshGallery(ArrayList<MyFavorite> favorites) {
        mRefresh.setRefreshing(false);
        mAdapter.refreshData(favorites,false);
    }

    @Override
    public void addMoreGallery(ArrayList<MyFavorite> favorites) {
        mAdapter.refreshData(favorites,true);
    }
}
