package com.joe.zatuji.module.favoritepage;

import android.content.Context;

import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.module.favoritepage.FavoriteModel;
import com.joe.zatuji.module.favoritepage.model.FavoriteKathy;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.module.favoritepage.view.TagView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.data.bean.User;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/4/19.
 */
public class FavoritePresenter extends BasePresenter<TagView,FavoriteModel> {

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
    }

    public void getFavoriteTag(){
        if(!MyApplication.isLogin()) {
            mView.showNotLogin();
            return;
        }
        mRxJavaManager.add(mModel.getAllTags(MyApplication.mUser.objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RxSubscriber<BaseListBean<FavoriteTag>>() {
            @Override
            public void onError(Throwable e) {
                mView.showToastMsg(e.getMessage());
            }

            @Override
            public void onNext(BaseListBean<FavoriteTag> favoriteTagBaseListBean) {
                if(favoriteTagBaseListBean.results!=null&& favoriteTagBaseListBean.results.size()>0){
//                    LogUtils.d("tag:"+favoriteTagBaseListBean.results.get(0).tag);
                    mView.showTag(favoriteTagBaseListBean.results);
                }else{
                    mView.showNoTag();
                    mView.showToastMsg("还没有图集噢～");
                }
            }
        }));
    }

    public void createTag(final FavoriteTag tag){
        mRxJavaManager.add(mModel.createTags(tag,MyApplication.mUser.objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                ArrayList<FavoriteTag> tags =new ArrayList<FavoriteTag>();
                tags.add(tag);
                mView.addTag(tags);
            }
        }));
//        mLoading.showLoading();
//        User user = MyApplication.mUser;
//        if(user == null || PrefUtils.getBoolean(mContext, Constant.IS_EXIT,false)){
//            onCreateError("请先登录账号");
//            return;
//        }
//        mKathy.createTag(tag,user);
    }
//    @Override
//    public void onSuccess(ArrayList<FavoriteTag> tags) {
//        mLoading.doneLoading();
//        mView.showTag(tags);
//    }
//
//    @Override
//    public void onError(String msg) {
//        mLoading.doneLoading();
//        mView.showErrorMsg(msg);
//    }
//
//    @Override
//    public void onCreateSuccess(ArrayList<FavoriteTag> tags) {
//        mLoading.doneLoading();
//        mView.addTag(tags);
//    }
//
//    @Override
//    public void onCreateError(String msg) {
//        mLoading.doneLoading();
//        KToast.show(msg);
//    }
//
//    @Override
//    public void onNotSign() {
//        mLoading.doneLoading();
//    }

}
