package com.joe.zatuji.module.loginpage.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.module.loginpage.model.LoginKathy;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.module.loginpage.view.FragmentView;
import com.joe.zatuji.module.loginpage.view.LoginView;

/**
 * Created by Joe on 2016/5/1.
 */
public class LoginPresenter implements LoginListener,ForgetListener,AvatarListener{
    private FragmentView mHomeView;
    private LoginView mLoginView;
    private Context mContext;
    private LoginKathy loginKathy;

    public LoginPresenter(Context mContext, LoginView mLoginView,FragmentView mHomeView) {
        this.mContext = mContext;
        this.mLoginView = mLoginView;
        this.mHomeView = mHomeView;
        loginKathy = new LoginKathy(mContext);
    }

    public void getCurrentUser(){
        User user = loginKathy.getCurrentUser();
        if(user!=null){
            mLoginView.setUserInfo(user);
            if(!TextUtils.isEmpty(user.getAvatar())) mLoginView.setAvatar(user.getAvatar());
        }
    }
    //登陆
    public void login(User user){
        loginKathy.login(user,this);
    }
    //忘记密码
    public void forgotPwd(String account){
        loginKathy.forgotPwd(account,this);
    }
    //注册
    public void jumpRegister(){
        mHomeView.changeFragment(null);
    }

    public void queryAvatar(String username){
        loginKathy.getAvatar(username,this);
    }

//    以下为回调
    @Override
    public void loginSuccess() {
        PrefUtils.putBoolean(mContext, Constant.IS_EXIT,false);
        mLoginView.doneLogin();
    }

    @Override
    public void loginError(String msg) {
        mLoginView.showErrorMsg(msg);
    }

    @Override
    public void onForgotSuccess() {
        mLoginView.resetPwdDone();
    }

    @Override
    public void onForgotError(String msg) {
        mLoginView.resetPwdError(msg);
    }

    @Override
    public void onAvatarGet(User user) {
        if(TextUtils.isEmpty(user.getAvatar())) return;
        mLoginView.setAvatar(user.getAvatar());
    }

    @Override
    public void onAvatarNull() {

    }
}
