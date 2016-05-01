package com.joe.zatuji.loginpager.presenter;

import android.content.Context;

import com.joe.zatuji.global.Constant;
import com.joe.zatuji.global.utils.PrefUtils;
import com.joe.zatuji.loginpager.model.RegisterKathy;
import com.joe.zatuji.loginpager.model.User;
import com.joe.zatuji.loginpager.view.RegisterView;

/**
 * Created by Joe on 2016/5/1.
 */
public class RegisterPresenter implements RegisterListener{
    private Context mContext;
    private RegisterKathy mKathy;
    private RegisterView mRegisterView;

    public RegisterPresenter(Context mContext, RegisterView mRegisterView) {
        this.mContext = mContext;
        this.mRegisterView = mRegisterView;
        this.mKathy = new RegisterKathy(mContext,this);
    }

    public void register(User user,String avatarPath){
        mKathy.register(user,avatarPath);
    }
    @Override
    public void onRegisterSuccess() {
        PrefUtils.putBoolean(mContext, Constant.IS_EXIT,false);
        mRegisterView.onSuccess();
    }

    @Override
    public void onRegisterError(String msg) {
        mRegisterView.onError(msg);
    }
}
