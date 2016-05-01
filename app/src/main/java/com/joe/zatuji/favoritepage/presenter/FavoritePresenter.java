package com.joe.zatuji.favoritepage.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.favoritepage.model.FavoriteKathy;
import com.joe.zatuji.favoritepage.model.FavoriteTag;
import com.joe.zatuji.favoritepage.view.TagView;
import com.joe.zatuji.global.Constant;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.global.utils.PrefUtils;
import com.joe.zatuji.homepage.presenter.HomeDataListener;
import com.joe.zatuji.homepage.view.HomeView;
import com.joe.zatuji.loginpager.model.User;

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
        User user = BmobUser.getCurrentUser(mContext,User.class);
        if(user == null || PrefUtils.getBoolean(mContext, Constant.IS_EXIT,false)){
            mView.showNotSign();
            return;
        }

        mKathy.getFavoriteTag(user);
    }

    public void createTag(FavoriteTag tag){
        User user = BmobUser.getCurrentUser(mContext,User.class);
        if(user == null || PrefUtils.getBoolean(mContext, Constant.IS_EXIT,false)){
            onCreateError("请先登录账号");
            return;
        }
        mKathy.createTag(tag);
    }
    @Override
    public void onSuccess(ArrayList<FavoriteTag> tags) {
        mView.showTag(tags);
    }

    @Override
    public void onError(String msg) {
        mView.showErrorMsg(msg);
    }

    @Override
    public void onCreateSuccess(ArrayList<FavoriteTag> tags) {
        mView.addTag(tags);
    }

    @Override
    public void onCreateError(String msg) {
        KToast.show(msg);
    }

    @Override
    public void onNotSign() {

    }
}
