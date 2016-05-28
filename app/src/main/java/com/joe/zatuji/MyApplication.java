package com.joe.zatuji;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by Joe on 2016/3/11.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    private RefWatcher refWatcher;//leakCanary
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        Bmob.initialize(this, Constant.BMOB_KEY);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //refWatcher = LeakCanary.install(this);
    }


    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
}
