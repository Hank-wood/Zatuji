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
import com.joe.zatuji.utils.KToast;
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
    public void autoCheckUpdate(){
        if(!SettingHelper.isCheckUpdate()) return;
        boolean isWifi = NetWorkUtils.getNetType(MyApplication.getInstance()) == NetWorkUtils.TYPE_WIFI;
        if(!SettingHelper.isCheckUpdateWithNoWifi()&&!isWifi) return;//如果不允许无wifi更新且当前不是wifi
        checkUpdate(false);
    }

    private void checkUpdate(boolean auto) {
        Api.getInstance()
                .mBmobService
                .checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<BaseListBean<UpdateBean>>() {
                    @Override
                    public void onError(ResultException e) {
                        KToast.show("检查更新出错啦");
                    }
                    @Override
                    public void onNext(BaseListBean<UpdateBean>results) {
                        if(results.results.size()>0){
                            UpdateBean updateBean = results.results.get(0);
                            if(updateBean.isforce){
                                startUpdate(updateBean.path);
                            }else if(updateBean.version_i>MyApplication.getInstance().getVersionCode()){
                                showUpdate(updateBean);
                            }

                        }
                    }
                });
    }

    private void startUpdate(UpdateBean.PathBean file) {

    }

    public void showUpdate(UpdateBean updateBean){

    }
    public void remove(){
        if(mRxJavaManger!=null)mRxJavaManger.remove();
    }
}
