package com.joe.zatuji.module.loginpage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.TokenBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.utils.FileUtils;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.io.File;
import java.util.Map;

import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.PATCH;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by joe on 16/5/29.
 */
public class LoginAndRegisterModel implements BaseModel {

    public Observable<TokenBean> register(User user){
        JsonElement json = GsonHelper.toJsonObject(user);
        LogUtils.d("register:\n"+json);
        return Api.getInstance()
                .mBmobService
                .register(json)
                .doOnNext(new Action1<TokenBean>() {
                    @Override
                    public void call(TokenBean tokenBean) {
                        LogUtils.d("token:"+tokenBean.sessionToken);
                        PrefUtils.putString(MyApplication.getInstance(), Constant.TOKEN,tokenBean.sessionToken);
                        Api.getInstance().updateToke(tokenBean.sessionToken);
                    }
                });
    }

    public Observable<User> login(User user){
        PrefUtils.putString(MyApplication.getInstance(),Constant.USER_NAME,user.username);
        PrefUtils.putString(MyApplication.getInstance(),Constant.PWD,user.password);
        return Api.getInstance()
                .mBmobService
                .login(user.username,user.password)
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {

                    }
                });
    }

    public Observable<BmobFile> upLoadFile(final String path){
        Observable<RequestBody> fileObservable = Observable.create(new Observable.OnSubscribe<RequestBody>() {
            @Override
            public void call(Subscriber<? super RequestBody> subscriber) {
                File file = new File(path);

                if(file.exists()) {
                    try {
                        LogUtils.d("二进制：\n"+new String(FileUtils.getFileToByte(file)));
                        RequestBody params=toRequestBody(file);
                        subscriber.onNext(params);
                        Api.getInstance().mBmobService.uploadFile("test2.txt","hello").subscribe();
                    }catch (Exception e){
                        subscriber.onError(new Throwable("transform fail"));
                    }
                }else{
                    subscriber.onError(new Throwable("file does not exist"));
                }
                subscriber.onCompleted();
            }
        });
        return fileObservable.flatMap(new Func1<RequestBody, Observable<BmobFile>>() {
            @Override
            public Observable<BmobFile> call(RequestBody params) {
                LogUtils.d(params.toString());
                return Api.getInstance().mBmobService.uploadFile("test.jpg",params);
            }
        });
    }

    public static RequestBody toRequestBody(File value) {
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), value);
        return body;
    }

}
