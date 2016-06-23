package com.joe.zatuji.module.gallerypage;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.data.bean.Pointer;
import com.joe.zatuji.data.bean.Relation;
import com.joe.zatuji.data.bean.RelationQuery;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.TableHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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

    public Observable<BaseBmobBean> setFront(String tagId,String url){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("front",url);
        return Api.getInstance().mBmobService.update("FavoriteTag",tagId,GsonHelper.toJsonObject(jsonObject));
    }

    public Observable<BaseBmobBean> removeImg(FavoriteTag tag,String imgId){
        Relation relation = new Relation();
        relation.remove();
        relation.objects.add(new Pointer("MyFavorite",imgId));
        Map<String,Object> map =new HashMap<>();
        map.put("img",relation);
        if(tag.number-1>=0){
            map.put("number",(tag.number-1));
            if(tag.number-1 == 0) map.put("front","");
        }

        return Api.getInstance().mBmobService.update("FavoriteTag",tag.objectId,GsonHelper.toJsonObject(map));
    }
}
