package com.joe.zatuji.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/5/29.
 */
public  class BaseBmobBean {
    public String objectId;
    public String updatedAt;
    public String createdAt;
    public Map<String,Increment> increment;
    public BaseBmobBean(){

    }
    public static class Increment{
        public String __op="Increment";
        public int amount;
        public Increment(int amount){
            this.amount = amount;
        }
    }
    public  String getTable(){
        return null;
    }
    //key1:{"__op":"Increment","amount":value}原子计数器
    public void Increment(String key,int amount){
        increment = new HashMap<>();
        increment.put(key,new Increment(amount));
    }
}
