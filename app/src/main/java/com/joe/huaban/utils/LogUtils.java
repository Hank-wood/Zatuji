package com.joe.huaban.utils;

import android.util.Log;

/**
 * Created by Joe on 2016/3/11.
 */
public class LogUtils {
    private static boolean islog=true;
    public static void Logout(String s){
        if(islog){
            Log.e("huaban",s);
        }
    }
}
