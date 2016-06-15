package com.joe.zatuji.data.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/6/11.
 */
public class RelationQuery implements Serializable{
    public Related $relatedTo;

    public static class Related{
        public Pointer object;//被关联的pointer 比如查询tag时 user就是object
        public String key;//被关联的字段

    }

    public RelationQuery(){
        this.$relatedTo = new Related();
    }
}
