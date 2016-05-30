package com.joe.zatuji.data;

/**
 * Created by joe on 16/5/29.
 */
public abstract class BaseBmobBean {
    public String objectId;
    public String updatedAt;
    public String createdAt;

    public abstract String getTable();
}
