package com.joe.zatuji.picdetail.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.picdetail.model.PicKathy;

/**
 * Created by Joe on 2016/4/18.
 */
public class PicDetailPresenter implements PicDetailListener{
    private Context context;
    private LoadingView loadingView;
    private final PicKathy mKathy;

    public PicDetailPresenter(Context context, LoadingView loadingView){
        this.context=context;
        this.loadingView=loadingView;
        mKathy = new PicKathy(context,this);
    }

    public void saveToFavorite(String picUrl,String desc,int width,int height){
        loadingView.showLoading();
        mKathy.saveToFavorite(picUrl,desc,width,height);
    }
    public void saveToPhone(String picUrl){
        loadingView.showLoading();
        mKathy.saveToPhone(picUrl);
    }
    public void share(String picUrl){

    }

    @Override
    public void onFavoriteSuccess() {
        loadingView.doneLoading();
        KToast.show("收藏成功");
    }

    @Override
    public void onFavoriteError(String errorMsg) {
        loadingView.doneLoading();
        KToast.show(errorMsg);
    }

    @Override
    public void onSaveSuccess() {
        loadingView.doneLoading();
        KToast.show("保存成功");
    }

    @Override
    public void onSaveError(String errorMsg) {
        loadingView.doneLoading();
        KToast.show(errorMsg);
    }
}
