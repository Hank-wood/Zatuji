package com.joe.zatuji;

/**
 * Created by Joe on 2016/3/11.
 */
public class Constant {
    public static final boolean IS_DEBUG =true;
    //接口地址
    public static final String HOST = "http://api.huaban.com/";//主机地址
    public static final String HOST_PIC = "http://img.hb.aicdn.com/";//图片保存地址
    public static final String HOST_TAG = "http://api.huaban.com/favorite/";
    public static final String HOST_SEARCH = "http://api.huaban.com/search/";

    //跳转携带数据
    public static final String PIC_DATA = "picData";//图片数据
    public static final String PIC_DESC = "picDesc";//图片描述
    public static final String PIC_WIDTH = "picWidth";//图片宽
    public static final String PIC_HEIGHT = "picHeight";//图片长

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

    //设置
    public static final String DEFAULT_TAG = "default_tag" ;//默认的发现tag
    public static final String CHECK_UPDATE = "check_update";
    public static final String UPDATE_NO_WIFI = "update_no_wifi";//boolean 无wifi也检查更新
    public static final String NOTIFY_NO_WIFI = "notify_no_wifi";//boolean 无wifi时提醒用户是否加载图片
    public static final String UPDATE_IGNORE = "update_ignore";//被用户忽略的版本
    public static final String AUTO_CLEAR = "auto_clear";//自动清理缓存

    //标记
    public static final String IS_OLD_TAG ="is_old_tag";//老的FavortiteTag没有userid字段

}

