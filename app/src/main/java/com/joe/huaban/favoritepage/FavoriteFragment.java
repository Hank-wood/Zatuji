package com.joe.huaban.favoritepage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.joe.huaban.HomeActivity;
import com.joe.huaban.R;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.base.ui.BaseFragment;
import com.joe.huaban.favoritepage.presenter.FavoritePresenter;
import com.joe.huaban.homepage.adapter.HomeAdapter;
import com.joe.huaban.homepage.view.HomeView;

/**
 * Created by Joe on 2016/4/18.
 */
public class FavoriteFragment extends BaseFragment implements HomeView{
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private FavoritePresenter mPresenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_stagerd_base;
    }

    @Override
    protected void initPresenter() {
        HomeActivity activity= (HomeActivity) mActivity;
        mPresenter = new FavoritePresenter(this,activity,myApplication);
        mPresenter.getFavoriteData();
    }

    @Override
    protected void initView() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_fragment_base);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_fragment_base);
        mAdapter = new HomeAdapter(mActivity);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFavoriteData();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = mLayoutManager.findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);

                if (dy > 0 && lastitem > mAdapter.getItemCount() - 5) {
                    mPresenter.loadMoreData();
                }
            }
        });
    }
    @Override
    public void refreshData(PicData data) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.refreshData(data,false);
    }

    @Override
    public void loadMore(PicData data) {
        mAdapter.refreshData(data,true);
    }
}
