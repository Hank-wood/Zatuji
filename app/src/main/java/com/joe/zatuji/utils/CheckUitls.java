package com.joe.zatuji.utils;

import android.text.TextUtils;

/**
 * 检查各种符合的工具类
 * Created by joe on 16/6/10.
 */
public class CheckUitls {
    public static boolean isEmailFormat(String email){
        if(TextUtils.isEmpty(email)) return false;
        if(!email.contains("@")||!email.contains(".")) return false;
        return true;
    }
}
