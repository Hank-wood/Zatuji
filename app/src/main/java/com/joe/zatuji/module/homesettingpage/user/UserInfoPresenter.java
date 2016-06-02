package com.joe.zatuji.module.homesettingpage.user;

import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.bean.User;

import rx.functions.Action1;

/**
 * 用户信息操作
 * Created by Joe on 2016/4/30.
 */
public class UserInfoPresenter extends BasePresenter<UserView,BaseModel> {
    @Override
    public void onStart() {
        //订阅登录和退出事件
        mRxJavaManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object user) {
                mView.setUserInfo((User) user);
            }
        });
        mRxJavaManager.subscribe(Event.LOGIN_OUT, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.setLoginStyle();
            }
        });
        mRxJavaManager.subscribe(Event.USER_UPDATE, new Action1<Object>() {
            @Override
            public void call(Object user) {
                mView.setUserInfo((User) user);
            }
        });
    }
    //获取当前用户信息
    public void getUserInfo(){
        if(MyApplication.isLogin()){
            mView.setUserInfo(MyApplication.mUser);
        }else{
            mView.setLoginStyle();
        }
    }

}
