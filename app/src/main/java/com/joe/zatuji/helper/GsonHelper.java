package com.joe.zatuji.helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * Created by joe on 16/5/21.
 */
public class GsonHelper {
    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    public static JsonElement toJsonObject(Object object){
        Gson gson = new Gson();
        return gson.toJsonTree(object);
    }

    public static <T> T fromJson(String json,Class<T> objectClass){
        Gson gson = new Gson();
        return gson.fromJson(json,objectClass);
    }

    public static <T> T fromJson(String json, Type T){
        Gson gson = new Gson();
        return gson.fromJson(json,T);
    }
}
