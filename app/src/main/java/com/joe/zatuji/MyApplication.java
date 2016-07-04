package com.joe.zatuji;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.AppUtils;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;


import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 全局
 * Created by Joe on 2016/3/11.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    private RefWatcher refWatcher;//leakCanary
    public TagBean.Tag mDefaultTag;
    public static User mUser;
    private RxJavaManager mRxManager;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        registerEvent();
        //MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(Constant.IS_DEBUG);
        //refWatcher = LeakCanary.install(this);
        mDefaultTag = new TagBean().tagList.get(SettingHelper.getDefaultTag());
        if(Constant.IS_DEBUG) LogUtils.d(AppUtils.getDeviceInfo(this));
        if(!PrefUtils.getBoolean(this,Constant.IS_EXIT,false)) autoLogin();
    }


    private void registerEvent() {
        mRxManager = new RxJavaManager();
        mRxManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object user) {
                PrefUtils.putBoolean(MyApplication.getInstance(), Constant.IS_EXIT,false);
                setUser((User) user);
            }
        });//登录
        mRxManager.subscribe(Event.LOGIN_OUT, new Action1<Object>() {
            @Override
            public void call(Object o) {
                PrefUtils.putBoolean(MyApplication.getInstance(), Constant.IS_EXIT,true);
                setUser(null);}
        });//退出
    }
    public void setUser(User user) {
        mUser = user;
    }
    //设置发现页的默认标签
    public void setDefaultTag() {mDefaultTag = new TagBean().tagList.get(PrefUtils.getInt(this, Constant.DEFAULT_TAG, 0));}

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public synchronized static MyApplication getInstance() {
        return myApplication;
    }

    public static boolean isLogin() {
        return mUser != null;
    }

    public int getVersionCode(){
        PackageManager packageManager = this.getPackageManager();
        int version = 0;
        try {
            version = packageManager.getPackageInfo(getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public String getVersionName(){
        PackageManager packageManager = this.getPackageManager();
        String  version = "1.0.0";
        try {
            version = packageManager.getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private void autoLogin() {
        User user = new User();
        user.username = PrefUtils.getString(this,Constant.USER_NAME,"");
        user.password = PrefUtils.getString(this,Constant.PWD,"");
        if(!TextUtils.isEmpty(user.username)){
            mRxManager.add(new LoginAndRegisterModel().login(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BmobSubscriber<User>() {
                        @Override
                        public void onError(ResultException e) {
                            LogUtils.d("auto login error:"+e.getError());
                        }

                        @Override
                        public void onNext(User user) {
                            mRxManager.post(Event.LOGIN_SUCCESS,user);
                        }
                    }));
        }

    }
}
