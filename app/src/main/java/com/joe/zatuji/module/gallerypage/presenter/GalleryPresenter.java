package com.joe.zatuji.module.gallerypage.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.module.gallerypage.model.GalleryKathy;
import com.joe.zatuji.module.gallerypage.view.GalleryView;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/2.
 */
public class GalleryPresenter implements GalleryListener{
    private Context mContext;
    private FavoriteTag tag;
    private GalleryKathy mKathy;
    private LoadingView mLoadingView;
    private GalleryView mView;
    private boolean noMoreGallery;

    public GalleryPresenter(Context mContext, FavoriteTag tag, LoadingView mLoadingView, GalleryView mView) {
        this.mContext = mContext;
        this.tag = tag;
        this.mLoadingView = mLoadingView;
        this.mView = mView;
        mKathy = new GalleryKathy(mContext,this);
        noMoreGallery = false;
    }

    private int limit = 20;
    private int page = 1;
    public void getGallery(){
        mLoadingView.showLoading();
        page = 1;
        mKathy.getGalleryData(tag,limit,page);
    }

    public void refreshGallery(){
        page = 1;
        mKathy.getGalleryData(tag,limit,page);
    }

    public void loadMoreGallery(){
        if(noMoreGallery) return;
        page++;
        mKathy.getGalleryData(tag,limit,page);
    }

    @Override
    public void onGetImgSuccess(boolean isMore, ArrayList<MyFavorite> favorites) {
        mLoadingView.doneLoading();
        noMoreGallery = favorites.size()<limit;
        if(isMore){
            mView.addMoreGallery(favorites);
        }else{
            mView.refreshGallery(favorites);
        }
    }

    @Override
    public void onGetImgError(boolean isMore,String msg) {
        mLoadingView.doneLoading();
        if(isMore){
            mView.showLoadError("没有更多数据了");
            noMoreGallery = true;
        }else{
            mView.showLoadError(msg);
        }
    }
}
