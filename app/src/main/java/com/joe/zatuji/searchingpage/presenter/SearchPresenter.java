package com.joe.zatuji.searchingpage.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.global.utils.LogUtils;
import com.joe.zatuji.homepage.presenter.HomeDataListener;
import com.joe.zatuji.homepage.view.HomeView;
import com.joe.zatuji.searchingpage.model.SearchKathy;

/**
 * 搜索
 * Created by Joe on 2016/4/20.
 */
public class SearchPresenter implements HomeDataListener{
    private Context context;
    private LoadingView loadingView;
    private HomeView mView;
    private int page=1;
    private boolean isLoadingMore;
    private final SearchKathy mKathy;
    private String query;
    private boolean noMoreData=false;

    public SearchPresenter(Context context, LoadingView loadingView, HomeView mView) {
        this.context = context;
        this.loadingView = loadingView;
        this.mView = mView;
        mKathy = new SearchKathy();
        isLoadingMore=false;
    }
    public void getData(String q){
        noMoreData=false;
        loadingView.showLoading();
        query = q;
        page=1;
        mKathy.getPicDataFromServer(q,page+"",this,false);
    }
    public void loadMoreData(){
        if(isLoadingMore) return;
        if(noMoreData) return;
        page++;
        mKathy.getPicDataFromServer(query,page+"",this,true);
        isLoadingMore=true;
    }
    @Override
    public void onSuccess(PicData result, boolean isLoadMore) {
        loadingView.doneLoading();
        isLoadingMore=false;
        LogUtils.d(result.pins.get(0).raw_text);
        LogUtils.d(result.pins.get(0).pin_id);
        if(isLoadMore){
            mView.loadMore(result);
        }else{
            mView.refreshData(result);
        }

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtils.d("请求数据失败："+ex.getMessage());
        loadingView.doneLoading();
        if(page>1){
            KToast.show("没有更多数据啦");
            page--;
            noMoreData=true;
        }else{
            loadingView.showError("寻觅失败(⊙﹏⊙) ");
        }
        isLoadingMore=false;
    }
}
