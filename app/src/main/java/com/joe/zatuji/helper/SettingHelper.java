package com.joe.zatuji.helper;

import android.content.pm.PackageManager;

import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.PrefUtils;

/**
 * Created by joe on 16/6/1.
 */
public class SettingHelper {
    /**是否允许自动更新*/
    public static boolean isCheckUpdate(){
        return PrefUtils.getBoolean(Constant.CHECK_UPDATE,true);
    }
    public static void setCheckUpdate(boolean checkUpdate){
        PrefUtils.putBoolean(Constant.CHECK_UPDATE,checkUpdate);
    }

    public static int getIgnoreVersion(){
        return PrefUtils.getInt(Constant.UPDATE_IGNORE,-1);
    }
    public static void setIgnoreVersion(int version){
        PrefUtils.putInt(Constant.UPDATE_IGNORE,version);
    }
    /**是否允许流量更新*/
    public static boolean isCheckUpdateWithNoWifi(){
        return PrefUtils.getBoolean(Constant.UPDATE_NO_WIFI,false);
    }
    public static void setCheckUpdateWithNoWifi(boolean noWifi){
        PrefUtils.putBoolean(Constant.UPDATE_NO_WIFI,noWifi);
    }

    /**2G状态是否提示*/
    public static boolean isNotifyNoWifi(){
        return PrefUtils.getBoolean(Constant.NOTIFY_NO_WIFI,true);
    }
    public static void setNotifyNoWifi(boolean isNotify){
        PrefUtils.putBoolean(Constant.NOTIFY_NO_WIFI,isNotify);
    }


    /**设置默认标签*/
    public static int getDefaultTag(){
        return PrefUtils.getInt(Constant.DEFAULT_TAG,3);
    }

    public static void setDefaultTag(int tag){
        PrefUtils.putInt(Constant.DEFAULT_TAG,tag);
    }
    /**设置自动清理*/
    public static void setAutoClear(boolean clear){PrefUtils.putBoolean(Constant.AUTO_CLEAR,clear);}

    public static boolean getAutoClear(){return PrefUtils.getBoolean(Constant.AUTO_CLEAR,false);}
}
