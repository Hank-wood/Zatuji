package com.joe.zatuji.module.loginpage.model;

import android.content.Context;

import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.module.loginpage.presenter.AvatarListener;
import com.joe.zatuji.module.loginpage.presenter.ForgetListener;
import com.joe.zatuji.module.loginpage.presenter.LoginListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Joe on 2016/5/1.
 */
public class LoginKathy {
    private Context mContext;

    public LoginKathy(Context mContext) {
        this.mContext = mContext;
    }
    public User getCurrentUser(){
        User bmobUser = BmobUser.getCurrentUser(mContext,User.class);
        return bmobUser;
    }
    public void login(User user, final LoginListener mListener){
//        user.login(mContext, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                mListener.loginSuccess();
//            }
//
//            @Override
//            public void onFailure(int i, String msg) {
//                mListener.loginError(msg);
//            }
//        });
    }

    public void forgotPwd(String email, final ForgetListener mListener){
//        User.resetPasswordByEmail(mContext, email, new ResetPasswordByEmailListener() {
//            @Override
//            public void onSuccess() {
//                mListener.onForgotSuccess();
//            }
//
//            @Override
//            public void onFailure(int i, String msg) {
//                mListener.onForgotError(msg);
//            }
//        });
    }

    public void getAvatar(String username, final AvatarListener mListener){
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", username);
        query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> users) {
                if(users.size()>0){
                    mListener.onAvatarGet(users.get(0));
                }
            }
            @Override
            public void onError(int code, String msg) {
                mListener.onAvatarNull();
            }
        });
    }
}
