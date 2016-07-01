package com.joe.zatuji.module.searchingpage;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ActionBar mActionBar;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_searching;
    }

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if(mActionBar!=null) mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setTitle("");
        mSearch = (SearchView) findViewById(R.id.search_view);
        mSearch.setIconified(false);
        mSearch.clearFocus();
    }


    @Override
    protected void initListener() {
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchingFragment.query(query);
                mActionBar.setTitle(query);
                mSearch.setQuery("",false);
                mSearch.setIconified(true);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionBar.setTitle("");
                mActionBar.setDisplayHomeAsUpEnabled(false);
            }
        });
    }

}
