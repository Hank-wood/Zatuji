package com.joe.zatuji.module.favoritepage;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.module.homepage.HomeActivity;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.module.favoritepage.adapter.FavoriteTagAdapter;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.view.CreateTagDialog;
import com.joe.zatuji.module.gallerypage.GalleryActivity;
import com.joe.zatuji.view.LockTagDialog;
import com.joe.zatuji.module.favoritepage.view.TagView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/4/18.
 */
public class FavoriteFragment extends BaseFragment<FavoritePresenter> implements TagView{
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private HomeActivity activity;
    private FavoriteTagAdapter mAdapter;
    private static FavoriteFragment mInstance;
    private TextView mEmptyView;

    public static synchronized FavoriteFragment getInstance(){
        if(mInstance==null){
            mInstance = new FavoriteFragment();
        }
        return mInstance;
    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initPresenter() {
        activity = (HomeActivity) mActivity;
        mPresenter.setView(this);
        showLoading("获取图集...");
        mPresenter.getFavoriteTag();
    }

    @Override
    protected void initView() {
        mEmptyView = (TextView) findView(R.id.tv_empty);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_fragment_base);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_fragment_base);
        mAdapter = new FavoriteTagAdapter(mActivity);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFavoriteTag();
            }
        });
        //点击item
        mAdapter.setOnItemClickListener(new FavoriteTagAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position, FavoriteTag tag) {
                //判断是否上锁
                if(tag.is_lock){
                    showPwdDialog(tag);
                }else{
                    jumpFavoriteDetail(tag);
                }
            }
        });
        //长按item
        mAdapter.setOnItemLongClickListener(new FavoriteTagAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClickListener(int position, FavoriteTag tag) {

            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0) {
                    activity.hideOrShowFAB(true);
                }else{
                    activity.hideOrShowFAB(false);
                }
            }
        });


    }
    //调到图集详情页
    private void jumpFavoriteDetail(FavoriteTag tag) {
        Intent i = new Intent(mActivity, GalleryActivity.class);
//        i.putExtra(Constant.GALLERY_TAG,tag);
        mActivity.startActivity(i);
    }

    //带锁的图集需要密码
    private void showPwdDialog(final FavoriteTag tag) {
        LockTagDialog dialog = new LockTagDialog(mActivity,tag);
        dialog.setOnPwdListener(new LockTagDialog.OnPwdListener() {
            @Override
            public void OnSuccess(LockTagDialog dialog) {
                //跳转到图集列表页
                jumpFavoriteDetail(tag);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showCreateTag() {
        if(!MyApplication.isLogin()){
            showToastMsg("请先登录噢～");
            return;
        }
        CreateTagDialog dialog = new CreateTagDialog(mActivity);
        dialog.setOnCreateCallBack(new CreateTagDialog.OnCreateCallBack() {
            @Override
            public void OnCreate(FavoriteTag tag) {
                mPresenter.createTag(tag);
            }
        });
        dialog.show();
    }


    @Override
    public void showTag(ArrayList<FavoriteTag> tags) {
        doneLoading();
        showEmpty(false);
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(tags,false);
    }

    @Override
    public void showNotLogin() {
        doneLoading();
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(new ArrayList<FavoriteTag>(),false);
        mEmptyView.setText("登录后可查看图集噢～");
        showEmpty(true);
    }

    @Override
    public void showNoTag() {
        doneLoading();
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(new ArrayList<FavoriteTag>(),false);
        mEmptyView.setText("还没有图集噢～赶快收藏吧！");
        showEmpty(true);
    }

    private void showEmpty(boolean b) {
        mEmptyView.setVisibility(b? View.VISIBLE:View.GONE);
    }



    @Override
    public void addTag(ArrayList<FavoriteTag> tags) {
        showEmpty(false);
        mAdapter.refreshData(tags,true);
    }

    @Override
    public void showToastMsg(String msg) {
        mRefreshLayout.setRefreshing(false);
        doneLoading();
        KToast.show(msg);
    }
}
