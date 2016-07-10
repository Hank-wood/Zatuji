package com.joe.zatuji.helper;

import android.content.Context;
import android.content.Intent;

import com.joe.zatuji.Event;
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
import com.joe.zatuji.view.UpdateDialog;
import com.umeng.analytics.MobclickAgent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/6/1.
 */
public class UpdateHelper {
    private RxJavaManager mRxJavaManger;
    private Context mContext;
    public UpdateHelper(Context context){
        this.mContext = context;
        mRxJavaManger = new RxJavaManager();
    }
    /**自动更新*/
    public void autoCheckUpdate(){
        if(!SettingHelper.isCheckUpdate()) return;
        boolean isWifi = NetWorkUtils.getNetType(MyApplication.getInstance()) == NetWorkUtils.TYPE_WIFI;
        if(!SettingHelper.isCheckUpdateWithNoWifi()&&!isWifi) return;//如果不允许无wifi更新且当前不是wifi
        checkUpdate(new UpdateSubscribe() {
            @Override
            public void onUpdate(UpdateBean updateBean) {
                if(updateBean==null){
                    //KToast.show("当前已是最新版～");什么也不干
                }else if (updateBean.isforce){
                    startUpdate(updateBean.path);
                }else if(updateBean.version_i==SettingHelper.getIgnoreVersion()){
                    return;
                }else {
                    showUpdate(updateBean);
                }
            }
        });
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
        MobclickAgent.onEvent(mContext, Event.EVENT_UPDATE);
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra("url",file.url);
        mContext.startService(intent);
    }
    /**显示更新的dialog*/
    public void showUpdate(UpdateBean updateBean){
        showUpdate(updateBean,mContext);
    }

    public void showUpdate(UpdateBean updateBean,Context Context){
        UpdateDialog dialog = new UpdateDialog(Context);
        dialog.setUpdateBean(updateBean);
        dialog.setOnUpdateListener(new UpdateDialog.onUpdateListener() {
            @Override
            public void onUpdate(UpdateBean updateBean) {
                startUpdate(updateBean.path);
                KToast.show("开始更新");
            }
        });
        dialog.show();
    }
    public void remove(){
        if(mRxJavaManger!=null)mRxJavaManger.remove();
    }

    public abstract static class  UpdateSubscribe extends BmobSubscriber<BaseListBean<UpdateBean>>{
        @Override
        public void onError(ResultException e){
            LogUtils.e("update:"+e.getMessage());
        };

        @Override
        public void onNext(BaseListBean<UpdateBean> results) {
            if(results.results.size()>0){
                UpdateBean updateBean = results.results.get(0);
                if(updateBean.version_i>MyApplication.getInstance().getVersionCode()){
                    onUpdate(updateBean);//回调更新
                }else{
                    onUpdate(null);
                }

            }
        }

        public abstract void onUpdate(UpdateBean updateBean);

    }
}
