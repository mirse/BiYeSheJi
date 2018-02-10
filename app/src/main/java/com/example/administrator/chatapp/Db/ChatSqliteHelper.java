package com.example.administrator.chatapp.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ChatSqliteHelper extends SQLiteOpenHelper {

    public ChatSqliteHelper(Context context) {
        super(context,"chatdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chathistory(_id integer primary key autoincrement,name varchar(20),contentChat varchar(255),data varchar(255),type varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
