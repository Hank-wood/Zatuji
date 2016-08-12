package com.joe.zatuji;

/**
 * Created by Joe on 2016/3/11.
 */
public class Constant {
    public static final boolean IS_DEBUG =true;

    //跳转携带数据
    public static final String PIC_DATA = "picData";//图片数据
    public static final String PIC_LIST = "picList";//图片列表
    public static final String PIC_FROM_GALLERY = "picFromGallery";//图片列表
    public static final String PIC_POS = "picPos";//图片位置

    public static final String GALLERY_TAG = "gallery_tag";//图片长

    //文件夹
    public static final String DIR_APP = "杂图集";//文件夹
    public static final String DIR_DOWNLOAD = "杂图集";//图片保存文件夹
    public static final String DIR_SHARE = "share";//图片保存文件夹
    public static final String BMOB_KEY = "396b7abd4336bd6e9564a34a310e2365";
    public static final String BMOB_REST = "6c6f1d60e461d29e814976fe2531b7b1";

    //广播
    public static final String LOGIN_SUCCESS = "login_success";
    //share
    public static final String SHARED_CONFIG = "config";
    public static final String IS_EXIT = "is_exit";

    //DataBase
    public static final String DATABASE = "Ztj";

    //Cache
    public static final String HOME = "home";


    //Bmob
    public static final String DEFAULT_AVATAR = "";//默认头像
    public static final String TOKEN = "token";
    public static final String USER_NAME = "user_name";
    public static final String PWD = "pwd";
    public static final String USER_AVATAR = "user_avatar";//用户头像

    //设置
    public static final String DEFAULT_TAG = "default_tag" ;//默认的发现tag
    public static final String CHECK_UPDATE = "check_update";
    public static final String UPDATE_NO_WIFI = "update_no_wifi";//boolean 无wifi也检查更新
    public static final String NOTIFY_NO_WIFI = "notify_no_wifi";//boolean 无wifi时提醒用户是否加载图片
    public static final String UPDATE_IGNORE = "update_ignore";//被用户忽略的版本
    public static final String AUTO_CLEAR = "auto_clear";//自动清理缓存
    public static final String DEBUG_MODE = "debug_mode";//开发者模式

    //标记
    public static final String IS_OLD_TAG ="is_old_tag";//老的FavortiteTag没有userid字段
    public static final String IS_FIRST_USE ="is_first_use";//第一次使用图集

    //welcome
    public static final String WELCOME_COVER = "welcome_cover";//欢迎页图片
    public static final String WELCOME_AUTHER = "welcome_auther";//欢迎页推荐人

}

