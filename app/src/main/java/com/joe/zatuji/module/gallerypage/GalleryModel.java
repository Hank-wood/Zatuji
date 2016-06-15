package com.joe.zatuji.module.gallerypage;

import com.google.gson.reflect.TypeToken;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.data.bean.Pointer;
import com.joe.zatuji.data.bean.RelationQuery;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.TableHelper;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * Created by joe on 16/6/12.
 */
public class GalleryModel implements BaseModel{
    public Observable<BaseListBean<MyFavorite>> getMyFavorite(FavoriteTag tag,String limit,int offset){
        TableHelper<BaseListBean<MyFavorite>> helper = new TableHelper<>();
        RelationQuery query = new RelationQuery();
        Pointer pointer = new Pointer("FavoriteTag",tag.objectId);
        query.$relatedTo.object = pointer;
        query.$relatedTo.key = "img";
        Type clazz = new TypeToken<BaseListBean<MyFavorite>>(){}.getType();
        return helper.query("MyFavorite", GsonHelper.toJsonObject(query),clazz,"-createAt",Integer.parseInt(limit),offset);
    }
}
