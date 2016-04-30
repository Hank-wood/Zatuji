package com.joe.zatuji.settingpage.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.joe.zatuji.loginpager.model.User;
import com.joe.zatuji.settingpage.model.FeedBack;
import com.joe.zatuji.settingpage.view.SettingView;
import com.joe.zatuji.settingpage.view.UserView;

/**
 * 用户信息操作
 * Created by Joe on 2016/4/30.
 */
public class UserInfoPresenter implements UserInfoListener{
    private Context mContext;
    private UserView mView;
    private SettingView mExit;
    public UserInfoPresenter(Context mContext, UserView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }
    public UserInfoPresenter(Context mContext, SettingView mExit) {
        this.mContext = mContext;
        this.mExit = mExit;
    }
    //获取当前用户信息
    public void getUserInfo(){

    }

    //退出登陆
    public void loginOut(){

    }

    //意见反馈
    public void feedBack(FeedBack feedBack){

    }
    //检查更新
    public void checkUpdate(){

    }

    /**
     *以下方法为回调
     */

    //当前有用户登陆，显示资料及退出
    @Override
    public void onUserLogin(User user) {
        if(TextUtils.isEmpty(user.getAvatar())){
            mView.setDefaultAvatar();
        }else{
            mView.setAvatar(user.getAvatar());
        }
        mView.setNick(user.getNickname());
        mExit.showExit();
    }

    //当前没有用户登陆，显示默认，隐藏退出
    @Override
    public void onUserNotLogin() {
        mView.setDefaultAvatar();
        mView.setLoginOrSignup();
        mExit.hideExit();
    }

    @Override
    public void onLogOutSuccess() {
        mView.setDefaultAvatar();
        mView.setLoginOrSignup();
        mExit.hideExit();
    }

    @Override
    public void onLogOutError() {
        mView.showError("退出失败，请检查网络");
    }

    @Override
    public void onFeedBackSuccess() {

    }

    @Override
    public void onFeedBackError() {

    }

}
