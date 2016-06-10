package com.joe.zatuji.module.userpage;

import android.text.TextUtils;

import com.joe.zatuji.Event;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.UserHelper;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.LogUtils;

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
    public void changePWD(final User user){
        mRxJavaManager.add(mModel.changePassword(user.objectId,user.password)
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
                UserHelper.saveCurrentPwd(user.password);
                mView.showToastMsg("修改密码成功！");
            }
        }));
    }
    public void changeAvatar(String path, final User user) {
        final String oldCdn = user.cdn;
        mRxJavaManager.add(new LoginAndRegisterModel()
                .upLoadAvatar(path)
                .flatMap(new Func1<BmobFile, Observable<BaseBmobBean>>() {
                    @Override
                    public Observable<BaseBmobBean> call(BmobFile bmobFile) {
                        user.avatar = bmobFile.url;
                        String url = bmobFile.url;
                        int start = url.indexOf(".com/")+(".com/").length();
                        user.cdn = bmobFile.cdn+"/"+url.substring(start);
                        mModel.changeCdn(user.cdn,user.objectId).subscribeOn(Schedulers.io()).subscribe();
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
                        if(!TextUtils.isEmpty(oldCdn)) mModel.deleteOldAvatar(oldCdn);
                        mRxJavaManager.post(Event.USER_UPDATE, user);
                        mView.updateAvatar(user.avatar);
                        mView.showToastMsg("上传成功");

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
                        mView.showToastMsg("昵称修改成功");
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
                        mView.showToastMsg("邮箱绑定成功！");
                    }
                }));
    }

}
