package com.joe.zatuji.module.searchingpage;

import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredModel;
import com.joe.zatuji.data.bean.DataBean;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/28.
 */
public class SearchModel extends BaseStaggeredModel {

    public Observable<DataBean> getData(int page,String per,String query){
        return Api.getInstance()
                .mApiService
                .search(page,per,query)
                .subscribeOn(Schedulers.io());
    }
}
