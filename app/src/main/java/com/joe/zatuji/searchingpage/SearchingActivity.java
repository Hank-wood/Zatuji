package com.joe.zatuji.searchingpage;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.SearchView;

import com.joe.zatuji.R;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.homepage.adapter.HomeAdapter;
import com.joe.zatuji.homepage.view.HomeView;
import com.joe.zatuji.searchingpage.presenter.SearchPresenter;

/**
 * Created by Joe on 2016/4/20.
 */
public class SearchingActivity extends BaseActivity implements HomeView{

    private SearchView mSearch;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private SearchPresenter mPresenter;

    @Override
    protected int getContent() {
        return R.layout.activity_searching;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SearchPresenter(mApplication,this,this);
    }

    @Override
    protected void initView() {
        mSearch = (SearchView) findViewById(R.id.search_view);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_search);
        mAdapter = new HomeAdapter(mActivity);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.getData(query);
                mRecyclerView.scrollToPosition(0);
                mSearch.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
        mAdapter.refreshData(data,false);
    }

    @Override
    public void loadMore(PicData data) {
        mAdapter.refreshData(data,true);
    }

    @Override
    public void stopRefresh() {

    }
}
