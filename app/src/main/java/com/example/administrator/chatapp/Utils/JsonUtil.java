package com.example.administrator.chatapp.Utils;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/1/15.
 */

public class JsonUtil {
    static Gson mgson;
    public static <T> T parseJson(String json,Class<T> tClass){
        if (mgson==null){
            mgson=new Gson();
        }
        if (TextUtils.isEmpty(json)){
            return null;
        }
        return mgson.fromJson(json,tClass);
    }
}
