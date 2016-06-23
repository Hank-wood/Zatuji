package com.joe.zatuji.module.favoritepage;


import android.text.TextUtils;

import com.joe.zatuji.Constant;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.BmobResponseBean;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

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
        mRxJavaManager.subscribe(Event.SET_FRONT, new Action1<Object>() {
            @Override
            public void call(Object o) {
                getFavoriteTag();
            }
        });
        mRxJavaManager.subscribe(Event.QUITE_GALLERY, new Action1<Object>() {
            @Override
            public void call(Object o) {
                getFavoriteTag();
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
                            if(PrefUtils.getBoolean(Constant.IS_OLD_TAG,true)) updateOldTag(favoriteTagBaseListBean.results);
                        } else {
                            mView.showNoTag();
                            mView.showToastMsg("还没有图集噢～");
                        }
                    }
                }));

    }

    private void updateOldTag(ArrayList<FavoriteTag> results) {
        for (FavoriteTag tag : results) {
            if (TextUtils.isEmpty(tag.user_id)){
                FavoriteTag tag1 = new FavoriteTag();
                tag1.is_lock = tag.is_lock;
                tag1.number = tag.number;
                tag1.user_id = MyApplication.mUser.objectId;
                mRxJavaManager.add(mModel.updateTag(tag1,tag.objectId).subscribeOn(Schedulers.io()).subscribe());
            }
        }
        PrefUtils.getBoolean(Constant.IS_OLD_TAG,false);
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

    public void updateTag(FavoriteTag tag,String objectId){
        mRxJavaManager.add(mModel.updateTag(tag,objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("编辑失败");
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                getFavoriteTag();
                mView.showToastMsg("编辑成功！");
            }
        }));
    }

    public void deleteTag(FavoriteTag tag){
        mRxJavaManager.add(mModel.deleteTag(tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BmobResponseBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("删除失败");
            }

            @Override
            public void onNext(BmobResponseBean bmobResponseBean) {
                mView.showToastMsg("删除成功");
                getFavoriteTag();
            }
        }));
    }
}
