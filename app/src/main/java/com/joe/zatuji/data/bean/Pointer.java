package com.joe.zatuji.data.bean;

import java.io.Serializable;

/**
 * Created by joe on 16/6/2.
 */
public class Pointer implements Serializable{
    public String __type="Pointer";
    public String className;
    public String objectId;
    public Pointer(String className,String objectId){
        this.className =className;
        this.objectId = objectId;
    };
}
