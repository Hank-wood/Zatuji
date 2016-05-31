package com.joe.zatuji.module.loginpage.register;

import com.joe.zatuji.Event;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.TokenBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.LogUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/29.
 */
public class RegisterPresenter extends BasePresenter<RegisterView, LoginAndRegisterModel> {
    @Override
    public void onStart() {
    }

    /**
     * 注册获取到token直接登录
     */
    public void register(final User user) {
        mRxJavaManager.add(mModel.register(user)
                .flatMap(new Func1<TokenBean, Observable<User>>() {
                    @Override
                    public Observable<User> call(TokenBean tokenBean) {
                        return mModel.login(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<User>() {
                    @Override
                    public void onError(ResultException e) {
                        LogUtils.d("error" + e.getError() + e.getCode());
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(User user) {
                        mView.showToastMsg("注册成功！");
                        mRxJavaManager.post(Event.LOGIN_SUCCESS, user);
                    }
                }));
    }

    public void uploadAvatar(String path) {
        mRxJavaManager.add(mModel.upLoadAvatar(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BmobFile>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onOtherError(Throwable e) {
                        mView.showToastMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(BmobFile bmobFile) {
                        LogUtils.d(bmobFile.toString());
                        mView.setUserAvatar(bmobFile.url);
                    }
                }));
    }
}
