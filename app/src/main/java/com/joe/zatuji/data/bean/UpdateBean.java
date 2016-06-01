package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBmobBean;

/**
 * Created by joe on 16/6/1.
 */
public class UpdateBean extends BaseBmobBean{

    /**
     * isforce : false
     * path : {"__type":"File","cdn":"upyun","filename":"atuji_1.0.1.apk","url":"http://bmob-cdn-916.b0.upaiyun.com/2016/05/02/45ceec82400a947d80e1bde99599eb6a.apk"}
     * platform : Android
     * target_size : 3655429
     * update_log : 1.修复新建图集失败bug
     * version : 杂图集1.0.1
     * version_i : 3
     */

    public boolean isforce;//是否强制更新
    public PathBean path; //apk信息
    public String platform; //android
    public String target_size;//大小 需要format一下
    public String update_log;//更新日志 \n换行
    public String version; //版本名称
    public int version_i;//版本号 只有高于当前才更新

    @Override
    public String getTable() {
        return "AppVersion";
    }

    /**
     * __type : File
     * cdn : upyun
     * filename : atuji_1.0.1.apk
     * url : http://bmob-cdn-916.b0.upaiyun.com/2016/05/02/45ceec82400a947d80e1bde99599eb6a.apk
     */
    public static class PathBean {
        public String __type; //表示是文件
        public String cdn;
        public String filename;//apk name
        public String url;//下载连接

    }
}
