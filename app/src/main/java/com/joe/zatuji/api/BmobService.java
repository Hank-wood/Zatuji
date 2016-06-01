package com.joe.zatuji.api;

import com.google.gson.JsonElement;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.BmobResponseBean;
import com.joe.zatuji.data.bean.BmobFile;
import com.joe.zatuji.data.bean.TokenBean;
import com.joe.zatuji.data.bean.UpdateBean;
import com.joe.zatuji.data.bean.User;


import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by joe on 16/5/21.
 */
public interface BmobService {
//以下为用户相关接口
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
    /**根据id查询用户*/
    @GET("1/users/{objectId}")
    Observable<User> getUserInfo(@Path("objectId")String userId);

    /**
     * 更新用户资料
     * params userID
     * params body json格式
     */
    @PUT("1/users/{objectId}")
    Observable<BaseBmobBean> updateUser(@Path("objectId")String userId,@Body JsonElement body);

    @GET("1/classes/_User")
    Observable<BaseListBean<User>> queryUser(@Query("where") JsonElement query);

    /**
     * 修改密码
     * params user_id
     * params new_password
     * params body json格式
     */
    @POST("1/functions/changePassword")
    Observable<BaseBmobBean> changePassword(@Body JsonElement query);

    @POST("1/requestPasswordReset")
    Observable<BaseBean> resetPassword(@Body JsonElement email);

    /**文件上传*/
    @Headers("Content-Type: image/jpeg")
    @POST("2/files/{fileName}")
    Observable<BmobFile> uploadAvatar(@Path("fileName")String fileName, @Body RequestBody file);
//以上为用户相关接口


//意见反馈接口
    /**意见反馈*/
    @POST("1/classes/FeedBackBean")
    Observable<BaseBmobBean> feedBack(@Body JsonElement feedback);

    @GET("1/classes/AppVersion")
    Observable<BaseListBean<UpdateBean>> checkUpdate();
}
