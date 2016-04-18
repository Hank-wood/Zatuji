package com.joe.huaban.favoritepage.presenter;

import android.content.Context;
import android.widget.Toast;

import com.joe.huaban.base.LoadingView;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.discoverpage.model.DiscoverKathy;
import com.joe.huaban.favoritepage.model.FavoriteKathy;
import com.joe.huaban.global.utils.KToast;
import com.joe.huaban.homepage.presenter.HomeDataListener;
import com.joe.huaban.homepage.view.HomeView;

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
        mKathy.getFavoriteData(currentPage,limit);
    }
    public void loadMoreData(){
        if(isLoadingMore) return;
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
        }else{
            KToast.show("你还没收藏哦");
        }
        isLoadingMore=false;
    }
}
