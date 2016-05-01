package com.joe.zatuji.favoritepage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.joe.zatuji.HomeActivity;
import com.joe.zatuji.R;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.favoritepage.adapter.FavoriteTagAdapter;
import com.joe.zatuji.favoritepage.model.FavoriteTag;
import com.joe.zatuji.favoritepage.presenter.FavoritePresenter;
import com.joe.zatuji.favoritepage.ui.CreateTagDialog;
import com.joe.zatuji.favoritepage.view.TagView;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.homepage.adapter.HomeAdapter;
import com.joe.zatuji.homepage.view.HomeView;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/4/18.
 */
public class FavoriteFragment extends BaseFragment implements TagView{
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private FavoritePresenter mPresenter;
    private HomeActivity activity;
    private FavoriteTagAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initPresenter() {
        activity = (HomeActivity) mActivity;
        mPresenter = new FavoritePresenter(this, activity,myApplication);
        mPresenter.getFavoriteTag();
    }

    @Override
    protected void initView() {
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
        mAdapter.setOnItemClickListener(new FavoriteTagAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position, FavoriteTag tag, boolean isCreate) {
                if(isCreate){
                    //新建tag
                    showCreateTag();
                }else{

                }
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

    private void showCreateTag() {
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
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(tags,false);
    }

    @Override
    public void showErrorMsg(String msg) {
        mRefreshLayout.setRefreshing(false);
        KToast.show(msg);
    }

    @Override
    public void showNotSign() {
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(new ArrayList<FavoriteTag>(),false);
    }

    @Override
    public void addTag(ArrayList<FavoriteTag> tags) {
        mAdapter.refreshData(tags,true);
    }
}
