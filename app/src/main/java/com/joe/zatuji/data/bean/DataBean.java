package com.joe.zatuji.data.bean;

import com.joe.zatuji.data.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 图片数据实体类
 * Created by Joe on 2016/4/13.
 */
public class DataBean extends BaseBean implements Serializable{
    /**
     * homedata.pins.get(position).pin_id is a key to request second page
     * homedata.pins.file.key is a img url
     */
    public List<PicBean> pins;

    public static class PicBean extends BaseBean implements Serializable{
        public String pin_id;
        public FileBean file;
        public String raw_text;

        public static class FileBean implements Serializable {
            public String key;
            public String type;
            public int width;
            public int height;
        }
    }
}
