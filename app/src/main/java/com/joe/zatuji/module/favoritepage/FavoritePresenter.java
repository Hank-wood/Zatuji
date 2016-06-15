package com.joe.zatuji.module.favoritepage;


import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.utils.LogUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/4/19.
 */
public class FavoritePresenter extends BasePresenter<TagView, FavoriteModel> {

    @Override
    public void onStart() {
        mRxJavaManager.subscribe(Event.LOGIN_OUT, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.showNotLogin();
            }
        });
        mRxJavaManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object o) {
                getFavoriteTag();
            }
        });
        mRxJavaManager.subscribe(Event.ADD_TAG, new Action1<Object>() {
            @Override
            public void call(Object tag) {
                ArrayList<FavoriteTag> tags = new ArrayList<FavoriteTag>();
                tags.add((FavoriteTag) tag);
                mView.addTag(tags);
            }
        });
        mRxJavaManager.subscribe(Event.ADD_FAVORITE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.setAddedNew(true);
            }
        });
    }

    public void getFavoriteTag() {
        if (!MyApplication.isLogin()) {
            mView.showNotLogin();
            return;
        }
        mRxJavaManager.add(Observable.concat(mModel.getAllTags(MyApplication.mUser.objectId)
                        .onErrorReturn(new Func1<Throwable, BaseListBean<FavoriteTag>>() {
                            @Override
                            public BaseListBean<FavoriteTag> call(Throwable throwable) {
                                LogUtils.e("error:"+throwable.getMessage());
                                return null;
                            }
                        })
                , mModel.getAllTagsCache(MyApplication.mUser.objectId))
                .subscribeOn(Schedulers.io())
                .first(new Func1<BaseListBean<FavoriteTag>, Boolean>() {
                    @Override
                    public Boolean call(BaseListBean<FavoriteTag> favoriteTagBaseListBean) {
                        LogUtils.d("first:"+(favoriteTagBaseListBean != null && favoriteTagBaseListBean.results != null));
                        return favoriteTagBaseListBean != null && favoriteTagBaseListBean.results != null;}
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseListBean<FavoriteTag>>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseListBean<FavoriteTag> favoriteTagBaseListBean) {
                        if (favoriteTagBaseListBean.results != null && favoriteTagBaseListBean.results.size() > 0) {
                            mView.showTag(favoriteTagBaseListBean.results);
                        } else {
                            mView.showNoTag();
                            mView.showToastMsg("还没有图集噢～");
                        }
                    }
                }));

    }

    public void createTag(final FavoriteTag tag) {
        mRxJavaManager.add(mModel.createTags(tag, MyApplication.mUser.objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseBmobBean>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseBmobBean baseBmobBean) {
                        ArrayList<FavoriteTag> tags = new ArrayList<FavoriteTag>();
                        tags.add(tag);
                        mView.addTag(tags);
                    }
                }));
    }

}
