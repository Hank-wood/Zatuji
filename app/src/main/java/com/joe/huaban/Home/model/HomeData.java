package com.joe.huaban.Home.model;

import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.ResponseParser;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by Joe on 2016/4/13.
 */
public class HomeData {
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
