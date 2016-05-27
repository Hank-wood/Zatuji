package com.joe.zatuji.utils;

import android.util.Log;

/**
 * Created by Joe on 2016/3/11.
 */
public class LogUtils {
    private static boolean isDebug=true;
    public static void e(String s){
        if(isDebug){
            Log.e("Zatuji",s);
        }
    }
    public static void d(String s){
        if(isDebug){
            Log.d("Zatuji",s);
        }
    }

    public static void api(String s){
        if(isDebug){
            Log.d("api",s);
        }
    }
}
