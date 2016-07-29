package com.joe.zatuji.module.searchingpage;

import com.joe.zatuji.Event;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 搜索列表结果
 * Created by joe on 16/5/28.
 */
public class SearchingFragment extends BaseStaggeredFragment<SearchPresenter> {
    public void query(String query){
        mRecyclerView.scrollToPosition(0);
        showLoading("搜索中...");
        Map<String,String> map = new HashMap<>();
        map.put("query",query);
        MobclickAgent.onEvent(mActivity, Event.EVENT_SEARCH,map);
        mPresenter.setQuery(query);
        mPresenter.loadData();
    }
}
