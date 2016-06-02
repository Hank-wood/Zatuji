package com.joe.zatuji.helper;

import com.joe.zatuji.Constant;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.utils.PrefUtils;

/**
 * Created by joe on 16/6/2.
 */
public class UserHelper {
    public static void saveCurrentUser(User user){
        PrefUtils.putString(Constant.USER_NAME, user.username);
        PrefUtils.putString(Constant.PWD, user.password);
    }
    public static void saveToken(String token){
        PrefUtils.putString(Constant.TOKEN,token);
    }

    public static String getToken(){
        return PrefUtils.getString(Constant.TOKEN,"");
    }

    public static String getCurrentUserName(){
        return PrefUtils.getString(Constant.USER_NAME,"");
    }

    public static String getCurrentUserPassword(){
        return PrefUtils.getString(Constant.PWD,"");
    }

    public static void saveCurrentPwd(String pwd){
        PrefUtils.putString(Constant.PWD, pwd);
    }
    public static void saveCurrentUserName(String userName){
        PrefUtils.putString(Constant.USER_NAME, userName);
    }
}
