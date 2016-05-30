package com.joe.zatuji.data.bean;

/**
 * Created by joe on 16/5/30.
 */
public class BmobFile {
    public String filename;
    public String url;
    public String cdn;

    @Override
    public String toString() {
        return "BmobFile{" +
                "filename='" + filename + '\'' +
                ", url='" + url + '\'' +
                ", cdn='" + cdn + '\'' +
                '}';
    }
}
