package com.example.administrator.chatapp.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.example.administrator.chatapp.Db.domain.ChatInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ChatDao {
    private Context context;
    private ChatSqliteHelper helper;
    private ChatInfo chatInfo;

    private ChatDao(Context context) {
        this.context=context;
        helper= new ChatSqliteHelper(context);
    }
    private static ChatDao chatDao=null;
    public static ChatDao getInstance(Context context){
        if (chatDao==null){
            chatDao=new ChatDao(context);
        }
        return chatDao;
    }
    public void insert(String name,String contentChat,String data,String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("contentChat",contentChat);
        values.put("data",data);
        values.put("type",type);
        db.insert("chathistory",null,values);
        db.close();
    }
    public List<ChatInfo> findAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("chathistory",new String[]{"name","contentChat","data","type"}, null, null, null, null, null);
        ArrayList<ChatInfo> chatlist = new ArrayList<>();

        while (cursor.moveToNext()){
            chatInfo = new ChatInfo();
//            Log.i("test",cursor.getString(0));
//            Log.i("test",cursor.getString(1));
            chatInfo.setName(cursor.getString(0));
            chatInfo.setContentChat(cursor.getString(1));
            chatInfo.setData(cursor.getString(2));
            chatInfo.setType(cursor.getString(3));
            chatlist.add(chatInfo);
        }

        cursor.close();
        db.close();
        return chatlist;
    }
}
