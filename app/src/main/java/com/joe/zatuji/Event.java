package com.joe.zatuji;

/**
 * 所有的事件名称
 * Created by joe on 16/5/28.
 */
public class Event {
    public static final String LOGIN_SUCCESS="login_success";//登录成功事件
    public static final String LOGIN_OUT="login_out";//退出
    public static final String USER_UPDATE="user_update";//用户资料更新
    //收藏
    public static final String ADD_TAG = "add_tag";//添加图集
    public static final String ADD_FAVORITE = "add_favorite";//收藏图片
    public static final String REMOVE_FAVORITE = "remove_favorite";//移除图片
    public static final String SET_FRONT = "set_front";//设置封面
    public static final String QUITE_GALLERY = "quite_gallery";//设置封面
}
