package com.example.administrator.chatapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.chatapp.Adapter.ChatHistoryAdapter;
import com.example.administrator.chatapp.Db.ChatDao;
import com.example.administrator.chatapp.Db.domain.ChatInfo;
import com.example.administrator.chatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ChatHisActivity extends Activity implements View.OnClickListener {

    private ListView his_lv;
    private ChatDao dao;
    private List<ChatInfo> mlist;
    private Button bt_next;
    private Button bt_last;
    private TextView tv_page;
    private static final int pageCount=8;
    private static int index=0;
    private ChatHistoryAdapter madapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chathis);
        dao=ChatDao.getInstance(getApplicationContext());
        initData();
        initUI();
        checkButton();

    }

    private void initData() {
        mlist = dao.findAll();
       Log.i("test",mlist.toString());
    }

    private void initUI() {
        his_lv = findViewById(R.id.his_lv);
        bt_next = findViewById(R.id.bt_next);
        bt_last = findViewById(R.id.bt_last);
        tv_page = findViewById(R.id.tv_page);
        findViewById(R.id.chathis_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_next.setOnClickListener(this);
        bt_last.setOnClickListener(this);
        tv_page.setText(index+1+"/"+(int) Math.ceil((double) mlist.size()/pageCount));
        madapter = new ChatHistoryAdapter(mlist, getApplicationContext(),index,pageCount);

        his_lv.setAdapter(madapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                Log.i("text1","点击下一页");
                nextPage();
                break;
            case R.id.bt_last:
                lastPage();
                break;
        }
    }
    private void lastPage() {
        index--;
        madapter = new ChatHistoryAdapter(mlist, getApplicationContext(),index,pageCount);
         his_lv.setAdapter(madapter);
        madapter.notifyDataSetChanged();
        tv_page.setText(index+1+"/"+(int) Math.ceil((double) mlist.size()/pageCount));
        checkButton();
    }
    private void nextPage() {
        //Log.i("text1","点击下一页111");
        index++;
        madapter = new ChatHistoryAdapter(mlist, getApplicationContext(),index,pageCount);
        his_lv.setAdapter(madapter);
        madapter.notifyDataSetChanged();
        tv_page.setText(index+1+"/"+(int) Math.ceil((double) mlist.size()/pageCount));
        checkButton();
    }
    private void checkButton(){
        if (index<=0){
            bt_last.setVisibility(View.INVISIBLE);
            bt_next.setVisibility(View.VISIBLE);
        }else if(mlist.size()-index*pageCount<=pageCount){
            bt_last.setVisibility(View.VISIBLE);
            bt_next.setVisibility(View.INVISIBLE);
        }
    }
}
