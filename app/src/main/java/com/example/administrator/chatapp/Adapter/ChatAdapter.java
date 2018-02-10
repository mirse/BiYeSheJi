package com.example.administrator.chatapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.chatapp.Bean.ChatMessage;
import com.example.administrator.chatapp.MainActivity;
import com.example.administrator.chatapp.R;
import com.example.administrator.chatapp.Utils.Constant;
import com.example.administrator.chatapp.Utils.SharedPrefsUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/11.
 */

public class ChatAdapter extends BaseAdapter {
    private ArrayList<ChatMessage> list;
    private LayoutInflater minflater;
    private Context context;
    private String robotname;
    public ChatAdapter(ArrayList<ChatMessage> list, Context context) {
        this.list = list;
        minflater=LayoutInflater.from(context);
        this.context =context;
    }
    public ChatAdapter(ArrayList<ChatMessage> list, Context context,String name) {
        this.list = list;
        minflater=LayoutInflater.from(context);
        this.context =context;
        this.robotname=name;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = list.get(position);
        if (chatMessage.getType().equals(ChatMessage.Type.OUT)){
            return 0;//发送消息
        }else{
            return 1;//接收消息
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String defusername = SharedPrefsUtil.getStringValue(context, Constant.USERNAME, "");
        //设置默认头像
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.defuserimg);
        Bitmap bitmap1 = SharedPrefsUtil.getBitmap(context, Constant.BITMAP, bitmap);

        ChatMessage chatMessage = list.get(position);
        ViewHolder mholder=null;
        if (convertView==null){
            if (getItemViewType(position)==0){
                mholder=new ViewHolder();
                convertView = minflater.inflate(R.layout.item_out,parent, false);
                mholder.tv=convertView.findViewById(R.id.tv_out);
                mholder.iv_user = convertView.findViewById(R.id.iv_user);
                mholder.time=convertView.findViewById(R.id.time_out);
                mholder.tv_chat_name=convertView.findViewById(R.id.tv_chat_name);
                mholder.iv_user.setImageBitmap(bitmap1);
                mholder.tv_chat_name.setText(defusername);
            }else{
                mholder=new ViewHolder();
                convertView= minflater.inflate(R.layout.item_in,parent,false);
                mholder.tv=convertView.findViewById(R.id.tv_in);
                mholder.tv_chat_name= convertView.findViewById(R.id.tv_chat_name);
                mholder.time=convertView.findViewById(R.id.time_in);
                mholder.tv_chat_name.setText(robotname);
            }
            convertView.setTag(mholder);

        }else{
          mholder = (ViewHolder) convertView.getTag();
        }
        if (chatMessage.getDate()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            mholder.time.setText(sdf.format(chatMessage.getDate()));
        }else {
            mholder.time.setVisibility(View.GONE);
        }
        mholder.tv.setText(chatMessage.getMsg());
//        String msg = chatMessage.getMsg();
//        if (msg.length()>15){
//            RelativeLayout.LayoutParams lm = (RelativeLayout.LayoutParams) mholder.tv.getLayoutParams();
//            lm.width=400;
//            mholder.tv.setLayoutParams(lm);
//        }
        return convertView;


    }
    static class ViewHolder{
        CircleImageView iv_user;
        TextView tv_chat_name;
        TextView time;
        TextView tv;
    }
}
