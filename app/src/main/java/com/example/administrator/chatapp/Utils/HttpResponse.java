package com.example.administrator.chatapp.Utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/1/15.
 */

public abstract class HttpResponse<T>
{
    Class<T> t;

    public HttpResponse(Class<T> t) {
        this.t = t;
    }
    public abstract void onSuccess(T t);
    public abstract void onFailure(String msg);
    public void parse(String json){
        if (TextUtils.isEmpty(json)){
            onFailure("请求失败");
        }
        if (t==String.class){
            onSuccess((T)json);
        }
        T result = JsonUtil.parseJson(json, this.t);
        if (result!=null){
            onSuccess(result);
        }else {
            onFailure("json解析失败");
        }
    }
}
