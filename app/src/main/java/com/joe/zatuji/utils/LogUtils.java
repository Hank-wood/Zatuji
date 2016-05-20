package com.joe.zatuji.utils;

import android.util.Log;

/**
 * Created by Joe on 2016/3/11.
 */
public class LogUtils {
    private static boolean isDebug=true;
    public static void e(String s){
        if(isDebug){
            Log.e("huaban",s);
        }
    }
    public static void d(String s){
        if(isDebug){
            Log.d("huaban",s);
        }
    }
}
