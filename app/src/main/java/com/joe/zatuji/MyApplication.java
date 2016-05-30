package com.joe.zatuji;

import android.app.Application;
import android.content.Context;

import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.data.bean.User;
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
        mDefaultTag = new TagBean().tagList.get(PrefUtils.getInt(this, Constant.DEFAULT_TAG, 0));
        LogUtils.d("default:" + PrefUtils.getInt(this, Constant.DEFAULT_TAG, 3) + ":" + mDefaultTag.requestName);
    }

    private void registerEvent() {
        mRxManager = new RxJavaManager();
        mRxManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object user) {setUser((User) user);
            }
        });
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



}
