package com.joe.zatuji;

import android.app.Application;

import com.joe.zatuji.global.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by Joe on 2016/3/11.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        Bmob.initialize(this, Constant.BMOB_KEY);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration );
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
