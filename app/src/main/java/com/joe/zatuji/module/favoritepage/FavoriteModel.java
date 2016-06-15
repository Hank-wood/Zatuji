package com.joe.zatuji.module.favoritepage;

import com.google.gson.reflect.TypeToken;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.dao.TagDao;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.Pointer;
import com.joe.zatuji.data.bean.Relation;
import com.joe.zatuji.data.bean.RelationQuery;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.TableHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 收藏model
 * Created by joe on 16/6/10.
 */
public class FavoriteModel implements BaseModel {
    public Observable<BaseListBean<FavoriteTag>> getAllTags(String userId){
        TableHelper<BaseListBean<FavoriteTag>> helper= new TableHelper<>();
        RelationQuery query = new RelationQuery();
        query.$relatedTo = new RelationQuery.Related();
        query.$relatedTo.key = "tag";
        query.$relatedTo.object = new Pointer("_User",userId);
        Type clazz = new TypeToken<BaseListBean<FavoriteTag>>(){}.getType();
        return helper.query("FavoriteTag", GsonHelper.toJsonObject(query),clazz,"-number")
                .doOnNext(new Action1<BaseListBean<FavoriteTag>>() {
                    @Override
                    public void call(BaseListBean<FavoriteTag> favoriteTagBaseListBean) {
                        if(favoriteTagBaseListBean.results!=null){
                            TagDao tagDao = new TagDao();
                            tagDao.clearCache();
                            tagDao.saveToCache(favoriteTagBaseListBean.results);
                        }
                    }
                });
    }
    public Observable<BaseListBean<FavoriteTag>> getAllTagsCache(final String user_id){
        return Observable.create(new Observable.OnSubscribe<BaseListBean<FavoriteTag>>() {
            @Override
            public void call(Subscriber<? super BaseListBean<FavoriteTag>> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                TagDao tagDao = new TagDao();
                BaseListBean<FavoriteTag> listBean = new BaseListBean<FavoriteTag>();
                listBean.results = tagDao.queryTag(user_id);
                if(listBean.results!=null) {
                    subscriber.onNext(listBean);
                }else {
                    subscriber.onError(new ResultException("加载图集失败"));
                }
                subscriber.onCompleted();
            }
        });
    }
    public Observable<BaseBmobBean> createTags(FavoriteTag tag, final String userId){
        return Api.getInstance().mBmobService.add("FavoriteTag",GsonHelper.toJsonObject(tag))
                .flatMap(new Func1<BaseBmobBean, Observable<BaseBmobBean>>() {
                    @Override
                    public Observable<BaseBmobBean> call(BaseBmobBean baseBmobBean) {
                        String tagId = baseBmobBean.objectId;
                        Relation add = new Relation();
                        add.objects.add(new Pointer("FavoriteTag",tagId));
                        add.add();
                        Map<String,Relation> map = new HashMap<String, Relation>();
                        map.put("tag",add);
                        return Api.getInstance().mBmobService.update("_User",userId,GsonHelper.toJsonObject(map));
                    }
                });
    }
}
