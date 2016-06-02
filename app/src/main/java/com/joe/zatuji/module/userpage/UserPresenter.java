package com.joe.zatuji.module.userpage;

import com.joe.zatuji.Event;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/6/2.
 */
public class UserPresenter extends BasePresenter<UserView, UserModel> {
    @Override
    public void onStart() {
    }

    public void changeAvatar(String path, final User user) {
        mRxJavaManager.add(new LoginAndRegisterModel()
                .upLoadAvatar(path)
                .flatMap(new Func1<BmobFile, Observable<BaseBmobBean>>() {
                    @Override
                    public Observable<BaseBmobBean> call(BmobFile bmobFile) {
                        user.avatar = bmobFile.url;
                        return mModel.changeAvatar(user.avatar, user.objectId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseBmobBean>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseBmobBean baseBmobBean) {
                        mRxJavaManager.post(Event.USER_UPDATE, user);
                        mView.updateAvatar(user.avatar);
                    }
                }));
    }

    public void changeNick(final User user) {
        mRxJavaManager.add(mModel
                .changeNick(user.nickname, user.objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseBmobBean>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseBmobBean baseBmobBean) {
                        mRxJavaManager.post(Event.USER_UPDATE, user);
                        mView.updateNickName(user.nickname);
                    }
                }));
    }

    public void changeEmail(final User user) {
        mRxJavaManager.add(mModel
                .changeEmail(user.email, user.objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseBmobBean>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg(e.getError());
                    }

                    @Override
                    public void onNext(BaseBmobBean baseBmobBean) {
                        mRxJavaManager.post(Event.USER_UPDATE,user);
                        mView.updateEmail(user.email);
                    }
                }));
    }

}
