package com.joe.zatuji.utils;

/**
 * Created by Joe on 2016/5/1.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;

//shareprefrences
public class PrefUtils {
    private static SharedPreferences mPref;

    //记录布尔类型
    public static void putBoolean(Context context,String key,Boolean value){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putBoolean(key, value).commit();
    }
    public static Boolean getBoolean(Context context,String key,Boolean defValue){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getBoolean(key, defValue);
    }
    public static void putString(Context context,String key,String value){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putString(key, value).commit();
    }
    //记录String类型
    public static String getString(Context context,String key,String defValue){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getString(key, defValue);
    }

    public static void putInt(Context context,String key,int value){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putInt(key, value).commit();
    }
    //记录int类型
    public static int getInt(Context context,String key,int defValue){
        mPref = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getInt(key, defValue);
    }

    /****/
    //记录布尔类型
    public static void putBoolean(String key,Boolean value){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putBoolean(key, value).commit();
    }
    public static Boolean getBoolean(String key,Boolean defValue){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getBoolean(key, defValue);
    }
    public static void putString(String key,String value){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putString(key, value).commit();
    }
    //记录String类型
    public static String getString(String key,String defValue){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getString(key, defValue);
    }

    public static void putInt(String key,int value){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        mPref.edit().putInt(key, value).commit();
    }
    //记录int类型
    public static int getInt(String key,int defValue){
        mPref = MyApplication.getInstance().getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return mPref.getInt(key, defValue);
    }
}