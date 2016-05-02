package com.joe.zatuji.settingpage.presenter;

import android.content.Context;

import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.global.utils.LogUtils;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

/**
 * Created by Joe on 2016/5/2.
 */
public class SettingPresenter {
    private Context context;

    public SettingPresenter(Context context) {
        this.context = context;
    }

    public void checkUpdate(){
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                LogUtils.d("status:"+i);
                if(i!=0) KToast.show("没有更新哦");
            }
        });
        BmobUpdateAgent.update(context);
    }
}
