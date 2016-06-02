package com.joe.zatuji.data.bean;

import java.util.List;

/**
 * Created by joe on 16/6/2.
 */
public class Relation {
    public String __op;
    public List<Pointer> objects;

    public void add(){
        this.__op = "AddRelation";
    }
    public void remove(){
        this.__op = "RemoveRelation";
    }
}
