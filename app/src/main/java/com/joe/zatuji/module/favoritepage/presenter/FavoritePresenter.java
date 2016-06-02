package com.joe.zatuji.module.favoritepage.presenter;

import android.content.Context;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.module.favoritepage.model.FavoriteKathy;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.module.favoritepage.view.TagView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.data.bean.User;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

/**
 * Created by Joe on 2016/4/19.
 */
public class FavoritePresenter implements FavoriteTagListener {
    private LoadingView mLoading;
    private FavoriteKathy mKathy;
    private TagView mView;
    private int currentPage;
    private boolean isLoadingMore;
    private boolean noMoreData=false;
    private Context mContext ;

    public FavoritePresenter(TagView mView, LoadingView mLoading, Context context){
        this.mView=mView;
        this.mLoading=mLoading;
        this.mContext = context;
        mKathy=new FavoriteKathy(context,this);
        currentPage=1;
        isLoadingMore=false;
    }

    public void getFavoriteTag(){
        mLoading.showLoading();
        User user = MyApplication.mUser;
        if(user == null || PrefUtils.getBoolean(mContext, Constant.IS_EXIT,false)){
            mView.showNotSign();
            mLoading.doneLoading();
            return;
        }

        mKathy.getFavoriteTag(user);
    }

    public void createTag(FavoriteTag tag){
        mLoading.showLoading();
        User user = MyApplication.mUser;
        if(user == null || PrefUtils.getBoolean(mContext, Constant.IS_EXIT,false)){
            onCreateError("请先登录账号");
            return;
        }
        mKathy.createTag(tag,user);
    }
    @Override
    public void onSuccess(ArrayList<FavoriteTag> tags) {
        mLoading.doneLoading();
        mView.showTag(tags);
    }

    @Override
    public void onError(String msg) {
        mLoading.doneLoading();
        mView.showErrorMsg(msg);
    }

    @Override
    public void onCreateSuccess(ArrayList<FavoriteTag> tags) {
        mLoading.doneLoading();
        mView.addTag(tags);
    }

    @Override
    public void onCreateError(String msg) {
        mLoading.doneLoading();
        KToast.show(msg);
    }

    @Override
    public void onNotSign() {
        mLoading.doneLoading();
    }
}
