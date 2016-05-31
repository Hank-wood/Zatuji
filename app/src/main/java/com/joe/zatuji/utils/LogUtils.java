package com.joe.zatuji.utils;

import android.util.Log;

import com.joe.zatuji.Constant;

/**
 * Created by Joe on 2016/3/11.
 */
public class LogUtils {

    public static void e(String s){
        if(Constant.IS_DEBUG){
            Log.e("Zatuji",s);
        }
    }
    public static void d(String s){
        if(Constant.IS_DEBUG){
            Log.d("Zatuji",s);
        }
    }

    public static void api(String s){
        if(Constant.IS_DEBUG){
            Log.d("api",s);
        }
    }
}
