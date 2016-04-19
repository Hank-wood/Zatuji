package com.joe.huaban.homepage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.joe.huaban.HomeActivity;
import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseFragment;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.homepage.adapter.HomeAdapter;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.homepage.presenter.HomePresenter;
import com.joe.huaban.homepage.presenter.HomePresenterImpl;
import com.joe.huaban.homepage.view.HomeView;

/**
 * 首页
 * Created by Joe on 2016/4/16.
 */
public class HomeFragment extends BaseFragment implements HomeView{

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;
    private HomePresenterImpl mPresenter;
    private HomeAdapter mAdapter;
    private StaggeredGridLayoutManager  mLayoutManager;
    private HomeActivity loadingView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_stagerd_base;
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
    protected void initPresenter() {
        loadingView = (HomeActivity) mActivity;
        mPresenter =  new HomePresenterImpl(this, loadingView,myApplication);
        //加载数据
        mPresenter.getCacheData();
    }


    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHomeData();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.d("y"+dy);
                if(dy>0) {
                    loadingView.hideOrShowFAB(true);
                }else{
                    loadingView.hideOrShowFAB(false);
                }
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
