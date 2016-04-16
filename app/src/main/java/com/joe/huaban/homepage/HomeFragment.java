package com.joe.huaban.homepage;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseFragment;
import com.joe.huaban.homepage.model.HomeData;
import com.joe.huaban.homepage.presenter.HomeDataListener;
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
    private HomePresenter mPresenter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_stagerd_base;
    }

    @Override
    protected void initView() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_fragment_base);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_fragment_base);

    }

    @Override
    protected void initPresenter() {
        mPresenter =  new HomePresenterImpl(this);
        //加载数据
        mPresenter.getHomeData();
    }

    @Override
    protected void destroyPresenter() {
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHomeData();
            }
        });
    }

    @Override
    public void refreshData(HomeData data) {

    }

    @Override
    public void loadMore(HomeData data) {

    }

}
