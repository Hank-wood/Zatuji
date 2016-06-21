package com.joe.zatuji.helper;

import android.app.Activity;
import android.app.IntentService;
import android.app.ListActivity;
import android.content.Intent;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.UpdateBean;
import com.joe.zatuji.helper.download.DownloadService;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.NetWorkUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/6/1.
 */
public class UpdateHelper {
    private RxJavaManager mRxJavaManger;
    private Activity mActivity;
    public UpdateHelper(Activity activity){
        this.mActivity = activity;
        mRxJavaManger = new RxJavaManager();
    }
    /**是否允许自动更新*/
    public boolean autoCheckUpdate(){
        if(!SettingHelper.isCheckUpdate()) return false;
        boolean isWifi = NetWorkUtils.getNetType(MyApplication.getInstance()) == NetWorkUtils.TYPE_WIFI;
        if(!SettingHelper.isCheckUpdateWithNoWifi()&&!isWifi) return false;//如果不允许无wifi更新且当前不是wifi
        return true;
    }
    /**检查是否有更新*/
    public void checkUpdate(BmobSubscriber<BaseListBean<UpdateBean>> subscriber) {
        Api.getInstance()
                .mBmobService
                .checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void startUpdate(UpdateBean.PathBean file) {
        Intent intent = new Intent(mActivity, DownloadService.class);
        intent.putExtra("url",file.url);
        mActivity.startService(intent);
    }

    public void showUpdate(UpdateBean updateBean){

    }
    public void remove(){
        if(mRxJavaManger!=null)mRxJavaManger.remove();
    }

    public abstract static class  UpdateSubscribe extends BmobSubscriber<BaseListBean<UpdateBean>>{
        @Override
        public void onError(ResultException e){
            LogUtils.e("update:"+e.getMessage());
            KToast.show("检查更新失败");
        };

        @Override
        public void onNext(BaseListBean<UpdateBean> results) {
            if(results.results.size()>0){
                UpdateBean updateBean = results.results.get(0);
                onUpdate(updateBean);
//                if(updateBean.version_i>MyApplication.getInstance().getVersionCode()){
//                    onUpdate(updateBean);//回调更新
//                }else{
//                    KToast.show("已是最新版本哦");
//                }

            }
        }

        public abstract void onUpdate(UpdateBean updateBean);

    }
}
