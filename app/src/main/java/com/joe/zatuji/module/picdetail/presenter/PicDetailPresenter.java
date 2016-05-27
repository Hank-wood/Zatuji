package com.joe.zatuji.module.picdetail.presenter;

import android.content.Context;

import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.module.favoritepage.model.FavoriteTag;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.module.loginpage.model.User;
import com.joe.zatuji.module.picdetail.model.PicKathy;

import cn.bmob.v3.BmobUser;

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

    public void saveToFavorite(String picUrl, String desc, int width, int height, FavoriteTag tag){
        loadingView.showLoading();
        User user = BmobUser.getCurrentUser(context,User.class);
        if(user == null ||PrefUtils.getBoolean(context,Constant.IS_EXIT,false)){
            KToast.show("请先登录账号");
            loadingView.doneLoading();
            return;
        }
        mKathy.addFavorite(picUrl,desc,width,height,tag,user);
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
