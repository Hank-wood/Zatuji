package com.joe.zatuji.data.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/6/11.
 */
public class RelationQuery {
    public Related $relatedTo;

    public static class Related{
        public Pointer object;
        public String key;

    }

    public RelationQuery(){
        this.$relatedTo = new Related();
    }
}
