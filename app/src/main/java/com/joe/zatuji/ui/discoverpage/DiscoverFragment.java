package com.joe.zatuji.ui.discoverpage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.joe.zatuji.ui.homepage.HomeActivity;
import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.PicData;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.ui.discoverpage.presenter.DiscoverPresenter;
import com.joe.zatuji.ui.homepage.adapter.HomeAdapter;
import com.joe.zatuji.ui.homepage.view.HomeView;

/**
 * Created by Joe on 2016/4/18.
 */
public class DiscoverFragment extends BaseFragment implements HomeView{
    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private DiscoverPresenter mPresenter;
    private HomeActivity loadingView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_stagerd_base;
    }

    @Override
    protected void initPresenter() {
        loadingView = (HomeActivity) mActivity;
        mPresenter = new DiscoverPresenter(this, loadingView,myApplication);
        mPresenter.getInitData();
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
                mPresenter.getInitData();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = mLayoutManager.findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);
                if(dy>0) {
                    loadingView.hideOrShowFAB(true);
                }else{
                    loadingView.hideOrShowFAB(false);
                }
                if (dy > 0 && lastitem > mAdapter.getItemCount() - 5) {
                    mPresenter.loadMore();
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

    @Override
    public void stopRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    public void loadTagData(int tag){
        mRecyclerView.scrollToPosition(0);
        mPresenter.getAnotherData(tag);

    }
}
