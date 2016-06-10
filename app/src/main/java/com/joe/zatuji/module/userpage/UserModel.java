package com.joe.zatuji.module.userpage;

import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.helper.GsonHelper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/6/1.
 */
public class UserModel implements BaseModel {
    /**修改密码*/
    public Observable<BaseBmobBean> changePassword(String user_id, String password){
        Map<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("new_password",password);
        return Api.getInstance()
                .mBmobService
                .changePassword(GsonHelper.toJsonObject(params));
    }
    /**修改用户信息*/
    public Observable<BaseBmobBean> updateUser(String key,String value,String userId){
        Map<String,String> params = new HashMap<>();
        params.put(key,value);
        return Api.getInstance()
                .mBmobService
                .updateUser(userId,GsonHelper.toJsonObject(params));
    }

    /**修改头像*/
    public Observable<BaseBmobBean> changeAvatar(String avatar,String userId){
        return updateUser("avatar",avatar,userId);
    }
    public Observable<BaseBmobBean> changeCdn(String cdn,String userId){
        return updateUser("cdn",cdn,userId);
    }
    /**修改昵称*/
    public Observable<BaseBmobBean> changeNick(String nick,String userId){
        return updateUser("nickname",nick,userId);
    }

    /**修改邮箱*/
    public Observable<BaseBmobBean> changeEmail(String email,String userId){
        return updateUser("email",email,userId);
    }

    public void deleteOldAvatar(String cdn){
        Api.getInstance().mBmobService.deleteAvatar(cdn).subscribeOn(Schedulers.io()).subscribe();
    }
}
