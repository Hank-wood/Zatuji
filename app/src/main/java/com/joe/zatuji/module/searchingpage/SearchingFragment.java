package com.joe.zatuji.module.searchingpage;

import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;

/**
 * 搜索列表结果
 * Created by joe on 16/5/28.
 */
public class SearchingFragment extends BaseStaggeredFragment<SearchPresenter> {
    public void query(String query){
        mRecyclerView.scrollToPosition(0);
        showLoading("搜索中...");
        mPresenter.setQuery(query);
        mPresenter.loadData();
    }
}
