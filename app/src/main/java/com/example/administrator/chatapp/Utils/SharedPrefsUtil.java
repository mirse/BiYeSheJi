package com.example.administrator.chatapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2018/1/22.
 */

public class SharedPrefsUtil {
    public final static String NAME = "QY";
    public static void putValue(Context context,String key,boolean value){
        context.getSharedPreferences(NAME,Context.MODE_PRIVATE).edit().putBoolean(key,value).commit();

    }
    public static boolean  getValue(Context context,String key,boolean defvalue){
        return context.getSharedPreferences(NAME,Context.MODE_PRIVATE).getBoolean(key,defvalue);
    }
    public static void putStringValue(Context context,String key,String value){
        context.getSharedPreferences(NAME,Context.MODE_PRIVATE).edit().putString(key,value).commit();

    }
    public static String  getStringValue(Context context,String key,String defvalue){
        return context.getSharedPreferences(NAME,Context.MODE_PRIVATE).getString(key,defvalue);
    }
    public static boolean putBitmap(Context context, String key, Bitmap bitmap){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        if (bitmap==null||bitmap.isRecycled()){
            return false;
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//可以捕获内存缓冲区的数据，转换成字节数组
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            //Bitmap.compress方法确实可以压缩图片，但压缩的是存储大小，即你放到disk上的大小

//            我尝试过把品质设置为10，decode出来的Bitmap大小没变，但显示照片的质量非常差
//
//            BitmapFactory.decodeByteArray方法对压缩后的byte[]解码后，得到的Bitmap大小依然和未压缩过一样
//
//            如果你想要显示的Bitmap占用的内存少一点，还是需要去设置加载的像素长度和宽度（变成缩略图）
            String imageBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));//译码
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(key,imageBase64);
            return edit.commit();
        }

    }
    public static Bitmap getBitmap(Context context,String key,Bitmap defaultValue){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String imageBase64 = sp.getString(key, "");
        if (TextUtils.isEmpty(imageBase64)){
            return defaultValue;
        }
        byte[] decode = Base64.decode(imageBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(decode);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);
        if (bitmap!=null){
            return bitmap;
        }else
            return defaultValue;
    }
}
