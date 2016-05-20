package com.joe.zatuji.api;

import com.joe.zatuji.data.bean.PicData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Joe on 16/5/18.
 */
public interface ApiService {

    /**
     * 首页图片数据max 最大id
     */
    @GET("favorite/photography/")
    Observable<PicData> getHomeData(@Query("limit") String limit,@Query("max") String max);

    /**
     * 发现页标签 tag标签 规则同首页数据
     */
    @GET("favorite/{tag}/")
    Observable<PicData> discoveryTag(@Path("tag") String tag,@Query("limit") String limit,@Query("max") String max);

    @GET("search/")
    Observable<PicData> search(@Query("page") int page,@Query("per_page") String per,@Query("q") String query);
}
