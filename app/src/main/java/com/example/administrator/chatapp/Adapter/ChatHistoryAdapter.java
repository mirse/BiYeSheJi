package com.example.administrator.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.chatapp.Db.domain.ChatInfo;
import com.example.administrator.chatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ChatHistoryAdapter extends BaseAdapter {
    private List<ChatInfo> mlist;
    private Context context;
    private ChatInfo chatInfo;
    private int index;
    private int pageCount;

    public ChatHistoryAdapter(List<ChatInfo> mlist, Context context,int index, int pageCount) {
        this.mlist = mlist;
        this.context=context;
        this.index=index;
        this.pageCount=pageCount;
    }

    @Override
    public int getCount() {
        //Log.i("text1",index+"");
        int current =index*pageCount;
        return mlist.size()-current<pageCount?mlist.size()-current:pageCount;
       // return mlist.size();
    }
    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position+index*pageCount;
        ViewHolder mholder =null;
        chatInfo = mlist.get(pos);
        if (convertView==null){
                mholder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.his_item_out, null);
                mholder.his_tv_username = convertView.findViewById(R.id.his_tv_username);
                mholder.his_tv_time = convertView.findViewById(R.id.his_tv_time);
                mholder.his_tv_content = convertView.findViewById(R.id.his_tv_content);
            convertView.setTag(mholder);
        }
        else {
            mholder = (ViewHolder) convertView.getTag();
        }
        //Log.i("test",chatInfo.toString());
        mholder.his_tv_username.setText(chatInfo.getName());
        mholder.his_tv_content.setText(chatInfo.getContentChat());
        mholder.his_tv_time.setText(chatInfo.getData());
        return convertView;
    }
    static class ViewHolder{
        TextView his_tv_username;
        TextView his_tv_time;
        TextView his_tv_content;
    }
}
