package com.example.administrator.chatapp.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/15.
 */

public class HttpUtils {
    static HttpUtils util;
    static OkHttpClient client;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    //构造方法
    private HttpUtils() {
        client=new OkHttpClient();
    }
    //单例方法
    public static HttpUtils getinstance(){
        if (util==null){
            synchronized (HttpUtils.class){
                if (util==null){
                    util=new HttpUtils();
                }
            }
        }
        return util;
    }
    public void getData(String url,String json,final HttpResponse respon){
        RequestBody requestBody = RequestBody.create(JSON,json);
//        FormBody body = new FormBody.Builder()
//                .add("key","855b2f1b0f1a4bb89967b812b415abe4")
//                .add("info", "你好")
//                .build();

        Request request=new Request.Builder()
                .url(url)
                //.addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();
        //开启一个异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    respon.onFailure("连接服务器失败");
                    return;
                }
                String date = response.body().string();
                respon.parse(date);
            }
        });
    }

}
