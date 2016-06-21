package com.joe.zatuji.module.loginpage.login;

import android.text.TextUtils;

import com.joe.zatuji.Constant;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.UserHelper;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/31.
 */
public class LoginPresenter extends BasePresenter<LoginView, LoginAndRegisterModel> {
    @Override
    public void onStart() {
    }

    public void login(User user) {
        mRxJavaManager.add(mModel.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<User>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(User user) {
                        mView.showToastMsg("登录成功！");
                        mRxJavaManager.post(Event.LOGIN_SUCCESS, user);
                    }
                }));
    }

    //重置密码
    public void resetPassword(String email) {
        mRxJavaManager.add(mModel.resetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseBean>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mView.resetPwdDone();
                    }
                }));
    }

    //根据用户名查询用户
    public void queryUserByName(String username) {
        mRxJavaManager.add(mModel.queryUserByName(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseListBean<User>>() {
                    @Override
                    public void onError(ResultException e) {
                        LogUtils.d(e.getError());
                    }

                    @Override
                    public void onNext(BaseListBean<User> baseListBean) {
                        List<User> list = baseListBean.results;
                        if(list.size()>0){
                            LogUtils.d(list.get(0).nickname);
                            mView.setAvatar(list.get(0).avatar);
                        }

                    }
                }));
    }

    //本地缓存的用户
    public void getCurrentUser() {
        String username = UserHelper.getCurrentUserName();
        if(TextUtils.isEmpty(username)) return;
        String pwd = UserHelper.getCurrentUserPassword();
        User user = new User();
        user.username = username;
        user.password = pwd;
        mView.setUserInfo(user);
    }
}
