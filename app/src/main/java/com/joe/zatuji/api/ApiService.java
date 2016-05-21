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
    //发现页 favorite/tag
    /**
     * 首页图片接口
     * params max 最大id
     */
    @GET("favorite/photography/")
    Observable<PicData> getHomeData(@Query("limit") String limit,@Query("max") String max);

    /**
     * 发现标签接口
     * params tag 标签（固定的那几个）
     * params limit  单页条数
     * params max 分页最大id
     */
    @GET("favorite/{tag}/")
    Observable<PicData> discoveryTag(@Path("tag") String tag,@Query("limit") String limit,@Query("max") String max);


    /**
     * 搜索接口
     * params page 页数
     * params per  单页条数
     * params query 查询关键字
     */
    @GET("search/")
    Observable<PicData> search(@Query("page") int page,@Query("per_page") String per,@Query("q") String query);

}
