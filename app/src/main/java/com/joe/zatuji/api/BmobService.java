package com.joe.zatuji.api;

import com.google.gson.JsonElement;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.TokenBean;
import com.joe.zatuji.data.bean.User;


import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by joe on 16/5/21.
 */
public interface BmobService {
    /**
     * params body 必须是json格式
     * {"username":"110@qq.com",
     *   "password":"123",
     *   "nickname":"haha"
     * }
     */
    @POST("1/users")
    Observable<TokenBean> register(@Body JsonElement body);
    /**
     * 登录
     */
    @GET("1/login")
    Observable<User> login(@Query("username") String username,@Query("password") String password);

    @GET("1/users/{objectId}")
    Observable<User> getUserInfo(@Path("objectId")String userId);

    /**
     * 更新用户资料
     * params userID
     * params body json格式
     */
    @PUT("1/users/{objectId}")
    Observable<BaseBmobBean> updateUser(@Path("objectId")String userId,@Body JsonElement body);


    /**文件上传*/

//    @Headers("Content-Type: image/jpeg")
    @Multipart
    @POST("2/files/{fileName}")
    Observable<BmobFile> uploadFile(@Path("fileName")String fileName, @Part("\"file\\\"; filename=\\\"image.jpg\\\"\"") RequestBody file);

    /**文件上传*/
    @Headers(
            "Content-Type: image/jpeg"
    )
    @POST("2/files/{fileName}")
    Observable<BmobFile> uploadFile(@Path("fileName")String fileName, @Body String file);
}
