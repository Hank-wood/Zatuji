package com.joe.huaban.homepage.model;

import com.joe.huaban.base.model.BaseData;

import java.util.List;

/**
 * 主页面数据实体类
 * Created by Joe on 2016/4/13.
 */
public class HomeData extends BaseData{
    /**
     * homedata.pins.get(position).pin_id is a key to request second page
     * homedata.pins.file.key is a img url
     */
    public List<PinsBean> pins;

    public static class PinsBean {
        public int pin_id;
        public FileBean file;

        public static class FileBean {
            public String key;
            public String type;
            public int width;
            public int height;
        }
    }
}
