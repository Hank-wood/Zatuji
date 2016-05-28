package com.joe.zatuji.module.searchingpage;

import android.os.Bundle;
import android.widget.SearchView;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;

/**
 * 搜索页面
 * Created by Joe on 2016/4/20.
 */
public class SearchingActivity extends BaseActivity{

    private SearchView mSearch;
    private SearchingFragment searchingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchingFragment = new SearchingFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_search, searchingFragment)
                .commit();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_searching;
    }

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        mSearch = (SearchView) findViewById(R.id.search_view);
    }

    @Override
    protected void initListener() {
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchingFragment.query(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
