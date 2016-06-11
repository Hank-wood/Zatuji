package com.joe.zatuji;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;
import com.squareup.leakcanary.RefWatcher;

import org.xutils.x;

import cn.bmob.v3.Bmob;
import rx.functions.Action1;

/**
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
        Bmob.initialize(this, Constant.BMOB_KEY);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        registerEvent();
        //refWatcher = LeakCanary.install(this);
        mDefaultTag = new TagBean().tagList.get(SettingHelper.getDefaultTag());
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
}
