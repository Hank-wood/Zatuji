package com.joe.zatuji.module.homesettingpage;

import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.bean.FeedBackBean;
import com.joe.zatuji.helper.GsonHelper;

import rx.Observable;

/**
 * Created by joe on 16/6/1.
 */
public class HomeSettingModel implements BaseModel {
    public Observable<BaseBmobBean> feedBack(FeedBackBean feedBack){
        return Api.getInstance()
                .mBmobService
                .feedBack(GsonHelper.toJsonObject(feedBack));
    }


}
