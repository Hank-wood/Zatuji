package com.joe.zatuji.module.homesettingpage;

import android.app.Activity;
import android.os.SystemClock;
import android.text.format.Formatter;

import com.bumptech.glide.Glide;
import com.joe.zatuji.Constant;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import org.xutils.x;

import java.io.File;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/5/2.
 */
public class HomeSettingPresenter extends BasePresenter<HomeSettingView, HomeSettingModel> {
    @Override
    public void onStart() {
        mRxJavaManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.showExit();
            }
        });//注册登录事件
        mRxJavaManager.subscribe(Event.LOGIN_OUT, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.hideExit();
            }
        });//注册退出事件
    }
//    private Context context;
//
//    public HomeSettingPresenter(Context context) {
//        this.context = context;
//    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                LogUtils.d("status:" + i);
                if (i != 0) KToast.show("没有更新哦");
            }
        });
//        BmobUpdateAgent.update(context);
    }

    /**
     * 退出
     */
    public void exit() {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(800);
                mRxJavaManager.post(Event.LOGIN_OUT, null);
            }
        }.start();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long start = System.currentTimeMillis();
                ImageHelper.clearCache();
                if((System.currentTimeMillis()-start)<2000)
                SystemClock.sleep(2000);
                subscriber.onNext("ok");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mView.showCache("0B");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d("error:"+throwable.getMessage());
                    }
                });
    }

    /**
     * 获取缓存
     */
    public void getCache(Activity activity) {

        File cacheDir = Glide.getPhotoCacheDir(activity);
        LogUtils.d("" + cacheDir.length());
        LogUtils.d("" + Formatter.formatFileSize(activity, cacheDir.length()));
        long size = 0;
        try {
            size = getFolderSize(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String cache = Formatter.formatFileSize(activity, size);
            mView.showCache(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}

