package com.joe.zatuji.helper;

import com.google.gson.Gson;

/**
 * Created by joe on 16/5/21.
 */
public class GsonHelper {
    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json,Class<T> objectClass){
        Gson gson = new Gson();
        return gson.fromJson(json,objectClass);
    }
}
