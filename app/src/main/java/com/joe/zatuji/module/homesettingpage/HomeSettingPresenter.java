package com.joe.zatuji.module.homesettingpage;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.text.format.Formatter;

import com.bumptech.glide.Glide;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.bean.FeedBackBean;
import com.joe.zatuji.data.bean.UpdateBean;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.helper.UpdateHelper;
import com.joe.zatuji.utils.LogUtils;


import java.io.File;

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
    private Context mContext;
    public void setContext(Context context){
        this.mContext = context;
    }
    /**
     * 检查更新
     */
    public void checkUpdate() {
        final UpdateHelper helper = new UpdateHelper(MyApplication.getInstance());
        helper.checkUpdate(new UpdateHelper.UpdateSubscribe() {
            @Override
            public void onUpdate(UpdateBean updateBean) {
                if(updateBean==null) {
                    mView.showToastMsg("当前已是最新版本～");
                    return;
                }
                helper.showUpdate(updateBean,mContext);
                mView.showToastMsg("发现新版本");
            }

            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("检查更新失败！");
            }
        });
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

    public void feedBack(FeedBackBean feedBackBean){
        mRxJavaManager.add(mModel.feedBack(feedBackBean)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg("哎呀，反馈失败了。还好保存了您的意见～");
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                mView.showToastMsg("反馈成功！感谢您宝贵的意见！");
            }
        }));
    }
}

