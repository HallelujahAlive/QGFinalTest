package com.qgtechforum.utils;

import com.google.gson.Gson;

//Json序列化(将Java对象转换为Json数据)和反序列化(将Json数据转换为Java对象)的工具类
public class JsonUtils {
    //使用Gson库来进行Json序列化和反序列化
    private static final Gson gson = new Gson();

    //T是泛型，使用T的好处就是可以将任意类型的Java对象转换为Json数据
    //clazz是Java对象的类型，使用Class<T>的好处就是可以将Java对象的类型作为参数传递给方法
    public static <T> T fromJson(String json, Class<T> clazz){
        //调用Gson方法，将Json数据转换为Java对象并返回这个对象
        return gson.fromJson(json, clazz);
    }

    //将Java对象转换为Json数据
    public static String toJson(Object obj){
        //调用Gson方法，将Java对象转换为Json数据并返回这个Json数据
        return gson.toJson(obj);
    }
}
