package com.joe.zatuji.module.gallerypage;


import com.joe.zatuji.Event;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredPresenter;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredView;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.module.favoritepage.FavoriteModel;
import com.joe.zatuji.utils.KToast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/5/2.
 */
public class GalleryPresenter extends BaseStaggeredPresenter<GalleryView,GalleryModel>{
    private FavoriteTag mTag;
    @Override
    public void onStart() {

    }

    public void loadData(FavoriteTag tag) {
        super.loadData();
        this.mTag = tag;
        mRxJavaManager.add(mModel.getMyFavorite(mTag,mLimit,mOffset)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseListBean<MyFavorite>>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseListBean<MyFavorite> myFavoriteBaseListBean) {
                countOffset(myFavoriteBaseListBean);
                if(myFavoriteBaseListBean.results!=null && myFavoriteBaseListBean.results.size()>0){
                    mView.loadData(myFavoriteBaseListBean.results);

                }else {
                    mView.showEmptyView();
                }
            }
        }));
    }

    public void reLoadData() {
        super.reLoadData();
        mRxJavaManager.add(mModel.getMyFavorite(mTag,mLimit,mOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseListBean<MyFavorite>>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseListBean<MyFavorite> myFavoriteBaseListBean) {
                        countOffset(myFavoriteBaseListBean);
                        if(myFavoriteBaseListBean.results!=null && myFavoriteBaseListBean.results.size()>0){
                            mView.refreshData(myFavoriteBaseListBean.results);

                        }else {
                            mView.showEmptyView();
                        }
                    }
                }));
    }

    @Override
    public void loadMoreData() {
        super.loadMoreData();
        mRxJavaManager.add(mModel.getMyFavorite(mTag,mLimit,mOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseListBean<MyFavorite>>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseListBean<MyFavorite> myFavoriteBaseListBean) {
                        countOffset(myFavoriteBaseListBean);
                        if(myFavoriteBaseListBean.results!=null && myFavoriteBaseListBean.results.size()>0){
                            mView.addData(myFavoriteBaseListBean.results);

                        }else {
                            showNoMoreData();
                        }
                    }
                }));
    }

    protected void countOffset(BaseListBean<MyFavorite> list ) {
        if(list!=null && list.results!=null &&list.results.size()>0) {
            mOffset += list.results.size();
            mPage++;
        }
    }

    public void setAsFront(FavoriteTag tag,String url){
        mRxJavaManager.add(mModel.setFront(tag.objectId,url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("设置失败");
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                mView.showToastMsg("设置成功");
                mRxJavaManager.post(Event.SET_FRONT,baseBmobBean);
            }
        }));
    }

    public void removeImg(FavoriteTag tag,MyFavorite img){
        mRxJavaManager.add(mModel.removeImg(tag,img.objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("移除失败");
                mView.removeItem(false);
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                mView.showToastMsg("移除成功");
                mView.removeItem(true);
                mRxJavaManager.post(Event.REMOVE_FAVORITE,baseBmobBean);
            }
        }));
    }
}
