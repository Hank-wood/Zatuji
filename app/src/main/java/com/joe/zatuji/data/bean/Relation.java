package com.joe.zatuji.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/6/2.
 */
public class Relation implements Serializable{
    public String __op;
    public List<Pointer> objects;

    public Relation(){
        this.objects = new ArrayList<>();
    }
    public void add(){
        this.__op = "AddRelation";
    }
    public void remove(){
        this.__op = "RemoveRelation";
    }

}
