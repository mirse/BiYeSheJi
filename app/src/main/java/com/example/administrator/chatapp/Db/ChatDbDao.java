package com.example.administrator.chatapp.Db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.chatapp.Bean.Question;
import com.example.administrator.chatapp.Utils.pattren_KMP;

/**
 * Created by Administrator on 2018/2/6.
 */

public class ChatDbDao {
    public static String path="data/data/com.example.administrator.chatapp/databases/chatdb.db";
    SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    private static Question que;

    //    public static String getAnswer(String question){
//
//
//    }
    public static Question findAnswer(String question){
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = sqLiteDatabase.query("mychatdata", new String[]{"key","answer"}, null, null, null, null, null);
            while (cursor.moveToNext()){
                que = new Question();
                String key = cursor.getString(0);
                int kmp = pattren_KMP.KMP(question, key);
                Log.i("test",kmp+"");
                if (kmp>0){
                    que.setKey(key);
                    String answer = cursor.getString(1);
                    que.setAnswer(answer);
                    return que;
                }


            }

            cursor.close();
            sqLiteDatabase.close();
        return que;

    }
}
