package com.joe.zatuji.module.loginpage;
import com.google.gson.JsonElement;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.TokenBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.helper.UserHelper;
import com.joe.zatuji.utils.FileUtils;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by joe on 16/5/29.
 */
public class LoginAndRegisterModel implements BaseModel {
    /**注册*/
    public Observable<TokenBean> register(User user) {
        JsonElement json = GsonHelper.toJsonObject(user);
        LogUtils.d("register:\n" + json);
        return Api.getInstance()
                .mBmobService
                .register(json)
                .doOnNext(new Action1<TokenBean>() {
                    @Override
                    public void call(TokenBean tokenBean) {
                        LogUtils.d("token:" + tokenBean.sessionToken);
                        UserHelper.saveToken(tokenBean.sessionToken);
                        Api.getInstance().updateToke(tokenBean.sessionToken);
                    }
                });
    }
    /**登录*/
    public Observable<User> login(User user) {
        UserHelper.saveCurrentUser(user);
        return Api.getInstance()
                .mBmobService
                .login(user.username, user.password)
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        UserHelper.saveToken(user.sessionToken);
                        Api.getInstance().updateToke(user.sessionToken);
                        MobclickAgent.onProfileSignIn(user.objectId);//统计用户
                    }
                });
    }
    /**上传头像*/
    public Observable<BmobFile> upLoadAvatar(final String path) {
        Observable<RequestBody> fileObservable = Observable.create(new Observable.OnSubscribe<RequestBody>() {
            @Override
            public void call(Subscriber<? super RequestBody> subscriber) {
                File file = new File(path);
                if (file.exists()) {
                    RequestBody params = FileUtils.toRequestBody(file);
                    subscriber.onNext(params);
                } else {
                    subscriber.onError(new Throwable("file does not exist"));
                }
                subscriber.onCompleted();
            }
        });
        return fileObservable.flatMap(new Func1<RequestBody, Observable<BmobFile>>() {
            @Override
            public Observable<BmobFile> call(RequestBody params) {
                return Api.getInstance().mBmobService.uploadAvatar(path.substring(path.lastIndexOf("/"), path.length()), params);
            }
        });
    }


    /**重置密码*/
    public Observable<BaseBean> resetPassword(String email){
        User user = new User();
        user.email = email;
        return Api.getInstance()
                .mBmobService
                .resetPassword(GsonHelper.toJsonObject(user));
    }

    public Observable<BaseListBean<User>> queryUserByName(String username){
        User user = new User();
        user.username = username;
        return Api.getInstance().mBmobService.queryUser(GsonHelper.toJsonObject(user));
    }
}
