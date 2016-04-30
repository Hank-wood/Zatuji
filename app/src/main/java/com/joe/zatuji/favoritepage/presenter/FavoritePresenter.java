package com.joe.zatuji.favoritepage.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.favoritepage.model.FavoriteKathy;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.homepage.presenter.HomeDataListener;
import com.joe.zatuji.homepage.view.HomeView;

/**
 * Created by Joe on 2016/4/19.
 */
public class FavoritePresenter implements HomeDataListener {
    private LoadingView mLoading;
    private FavoriteKathy mKathy;
    private HomeView mView;
    private int currentPage;
    private int limit=20;
    private boolean isLoadingMore;
    private boolean noMoreData=false;

    public FavoritePresenter(HomeView mView, LoadingView mLoading, Context context){
        this.mView=mView;
        this.mLoading=mLoading;
        mKathy=new FavoriteKathy(context,this);
        currentPage=1;
        isLoadingMore=false;
    }
    public void getFavoriteData(){
        currentPage=1;
        mLoading.showLoading();
        noMoreData=false;
        mKathy.getFavoriteData(currentPage,limit);
    }
    public void loadMoreData(){
        if(isLoadingMore) return;
        if(noMoreData) return;
        currentPage++;
        mKathy.getFavoriteData(currentPage,limit);
    }
    @Override
    public void onSuccess(PicData result, boolean isLoadMore) {
        mLoading.doneLoading();
        if(isLoadMore){
            mView.loadMore(result);
        }else{
            mView.refreshData(result);
        }
        isLoadingMore=false;
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        mLoading.doneLoading();
        if(currentPage>1){
            KToast.show("没有更多数据啦");
            currentPage--;
            noMoreData=true;
        }else{
            KToast.show("你还没收藏哦");
        }
        isLoadingMore=false;
    }
}
