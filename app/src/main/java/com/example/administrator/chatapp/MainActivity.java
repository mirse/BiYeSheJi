package com.example.administrator.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chatapp.Activity.AboutActivity;
import com.example.administrator.chatapp.Activity.ChatHisActivity;
import com.example.administrator.chatapp.Activity.SettingActivity;
import com.example.administrator.chatapp.Adapter.ChatAdapter;
import com.example.administrator.chatapp.Bean.ChatMessage;
import com.example.administrator.chatapp.Bean.MessageResult;
import com.example.administrator.chatapp.Bean.Question;
import com.example.administrator.chatapp.Db.ChatDao;
import com.example.administrator.chatapp.Db.ChatDbDao;
import com.example.administrator.chatapp.Utils.Constant;
import com.example.administrator.chatapp.Utils.Event;
import com.example.administrator.chatapp.Utils.HttpResponse;
import com.example.administrator.chatapp.Utils.HttpUtils;
import com.example.administrator.chatapp.Utils.SharedPrefsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_send;
    private EditText et_print;
    boolean isFocus = false;
    private RelativeLayout rl_bottom;
    private ListView lv_chat;
    private ArrayList<ChatMessage> mlist;
    //private ArrayList<ChatMessage> weatherlist;
    //private ArrayList<ChatMessage> mlist;
    private ChatAdapter madapter;
    private Date olddate;
    private Date newdate;
    private String Firoutmsg;
    private ImageView iv_more;
    private DrawerLayout drawerlayout;
    private ListView lv_menu;
    private ArrayList<String> menuList;//数据
    private ArrayAdapter<String> adapter;//适配器
    private ImageView iv_menu;
    private Toolbar toolsbar;
    private MyHandler mhandler;
    private RelativeLayout drawerlayout_content;
    private CircleImageView imagesetting;

    private static final String ISSHOWSCANGUIDE="ISSHOWSCANGUIDE";
    private TextView tv_username;
    private String username;
    private ChatDao dao;
    private File file;
    private boolean isFirst=true;
    private boolean isFirstWeather=true;
    private boolean isFirstInit=true;
    private String outmg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//布局上移显示键盘
        setContentView(R.layout.layout_slidemenu);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();//获取到了当前界面的DecorView,通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        mhandler=new MyHandler(this);
        dao=ChatDao.getInstance(getApplicationContext());
        initUI();
        initData("Hello!我是云打印智能客服，有什么问题都可以咨询我哦，你也可以选择其他客服（输入对应数字）:"+"\n"+"1:天气查询"+"\n"+"2:公交都知道","客服一号");
            CopyDb("chatdb.db");
        // getData(outmsg);
        //initMsg();
    }

    public String CopyDb(String SqlName)  {
        File dir = new File("data/data/" + this.getPackageName() + "/databases");
        if (!dir.exists()||!dir.isDirectory()){
            dir.mkdir();
        }
        file = new File(dir, SqlName);
        InputStream is =null;
        OutputStream os =null;
        if (!file.exists()){
            try {
                file.createNewFile();
                is = getAssets().open(SqlName);
                os=new FileOutputStream(file);
                byte[] buffer=new byte[1024];
                int len=-1;
                while ((len=is.read(buffer))!=-1){
                    os.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
               if (is!=null&&os!=null){
                   try {
                       is.close();
                       os.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
            }
        }
        return file.getPath();
    }

    /**
     * 加载布局
     */
    private void initUI() {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final int screenwidth = wm.getDefaultDisplay().getWidth();
        final int screenheight = wm.getDefaultDisplay().getHeight();


        rl_bottom = findViewById(R.id.rl_bottom);
        bt_send = findViewById(R.id.bt_send);
        et_print = findViewById(R.id.et_print);
        lv_chat = findViewById(R.id.lv_chat);
        iv_more = findViewById(R.id.iv_more);
        lv_menu = findViewById(R.id.lv_menu);

        iv_menu = findViewById(R.id.iv_menu);

        toolsbar = findViewById(R.id.toolsbar);
        drawerlayout_content = findViewById(R.id.drawerlayout_content);

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(drawerlayout_content);
            }
        });


        toolsbar.setTitle("");
        setSupportActionBar(toolsbar);
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerlayout = findViewById(R.id.drawerlayout);
        menuList=new ArrayList<String>();





        String[] menuList = getResources().getStringArray(R.array.menu_array);
//        for(int i=0;i<5;i++)
//            menuList.add("菜单"+i);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,menuList);

        View headview = LayoutInflater.from(this).inflate(R.layout.layout_headicon, null);
        //设置默认头像
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.defuserimg);
        Bitmap bitmap1 = SharedPrefsUtil.getBitmap(getApplicationContext(), Constant.BITMAP, bitmap);

        imagesetting = headview.findViewById(R.id.imagesetting);

        imagesetting.setImageBitmap(bitmap1);
        tv_username = headview.findViewById(R.id.tv_username);
        String defusername = SharedPrefsUtil.getStringValue(getApplicationContext(), Constant.USERNAME, "");
        tv_username.setText(defusername);
        imagesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,SettingActivity.class);
               startActivityForResult(intent, Constant.GETUSERNAME);
                drawerlayout.closeDrawers();
            }
        });
        lv_menu.addHeaderView(headview);
        lv_menu.setAdapter(adapter);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        Log.i("test","点击了聊天历史");
                        Intent intent =new Intent();
                        intent.setClass(MainActivity.this,ChatHisActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Log.i("test","点击了退出登录");
                        break;
                    case 3:
                        Log.i("test","点击了关于");
                        Intent intent1 =new Intent();
                        intent1.setClass(MainActivity.this,AboutActivity.class);
                        startActivity(intent1);

                }
            }
        });
        taggle();
        lv_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeymap();
                return false;
            }
        });
        bt_send.setOnClickListener(MainActivity.this);
        final Drawable leftdraw = getResources().getDrawable(R.drawable.biz_pc_main_tie_icon);
        leftdraw.setBounds(0, 0, 70, 70);
        et_print.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isFocus = hasFocus;
                if (hasFocus) {
                    //有焦点
                    iv_more.setVisibility(View.GONE);
                    bt_send.setVisibility(View.VISIBLE);
                    et_print.setCompoundDrawables(null, null, null, null);
                    et_print.setHint("");
                } else {
                    //无焦点
                    iv_more.setVisibility(View.VISIBLE);
                    bt_send.setVisibility(View.GONE);
                    et_print.setCompoundDrawables(leftdraw, null, null, null);
                    et_print.setHint("请输入文字.....");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case Constant.GETUSERNAME:
                username = data.getStringExtra("UN");
                byte[] photos = data.getByteArrayExtra("PHOTO");
                Bitmap photo = BitmapFactory.decodeByteArray(photos, 0, photos.length);
                imagesetting.setImageBitmap(photo);
                tv_username.setText(username);
                break;
        }
    }

    private void taggle() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolsbar, R.string.open, R.string.close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        toolsbar.setNavigationIcon(null);//设置toolbar导航图标


        drawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                float translationX = drawerView.getTranslationX();
//                int scrollX = drawerView.getScrollX();
//                Log.i("test","translationX"+translationX+"");
//                Log.i("test","scrollx"+scrollX+"");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                et_print.setFocusable(false);
                et_print.setFocusableInTouchMode(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                et_print.setFocusable(true);
                et_print.setFocusableInTouchMode(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
//        iv_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerlayout.openDrawer(Gravity.RIGHT);
//            }
//        });
    }

    private void hideKeymap() {
                InputMethodManager inputMethodManager  = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);//第一个参数是一个View的windowToken。第二个参数是软键盘隐藏时的控制参数。
    }

    /**
     * 初始化数据
     */
    private void initData(String Msg,String robotname) {
        mlist = new ArrayList<>();//输入文本集合
        olddate =new Date();
        mlist.add(new ChatMessage(Msg,olddate, ChatMessage.Type.IN));
        madapter = new ChatAdapter(mlist, this,robotname);
        lv_chat.setAdapter(madapter);
    }


    /**
     * 按下返回键
     */
    @Override
    public void onBackPressed() {
        if (isFocus) {
            rl_bottom.requestFocus();
        } else {
            finish();
        }
    }
    /**send按键发送后，时间及内容判断
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send:
                //判断是否是第一次输入
                if (isFirst){
                    Firoutmsg = et_print.getText().toString().trim();
                    isFirst=false;
                }

                //Log.i("test","数据导入成功");
//                StringBuilder sb = new StringBuilder(outmsg);
//                if (outmsg.length()>10){
//                    sb.insert(10, "\n");
//                    outmsg=sb.toString();
//                    outmsg.replace("\\n","\n");
//                }
                outmg = et_print.getText().toString().trim();
                if (TextUtils.isEmpty(Firoutmsg)||TextUtils.isEmpty(outmg)) {
                    Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                newdate=new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                String newtime = simpleDateFormat.format(newdate);
                dao.insert("用户",Firoutmsg,newtime,"OUT");
                if (Firoutmsg.equals("1")){
                    if (isFirstInit){
                        et_print.setText("");
                        initData("Hello,我是萌萌哒的天气客服!","天气客服");
                        isFirstInit=false;
                    }

                    if (!isFirstWeather) {
                        CheckTime();
                        getData(outmg);

                    }
                    isFirstWeather = false;
                }
                else if (Firoutmsg.equals("2")){
                    initData("等车很烦吧，找我就对了!","公车客服");
                }
                else{
                    CheckTime();
                    try {
                        Thread.sleep(300);
                        Question answer = ChatDbDao.findAnswer(outmg);
                        String answer1 = answer.getAnswer();
                        // Log.i("answer1",answer1);
                        Message msg=mhandler.obtainMessage(0);
                        // msg.arg1=0;
                        msg.obj=answer1;
                        mhandler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

               // dao.insert("发送时间",newtime);

                break;
        }



    }

    private void CheckTime() {
        long olddateTime = olddate.getTime();
        long newdateTime = newdate.getTime();

        long timelag = (newdateTime - olddateTime) / (1000 * 60);
        if (timelag>1){
            Log.i("time","超过一分钟");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMsg(outmg);
            chatMessage.setDate(newdate);
            chatMessage.setType(ChatMessage.Type.OUT);
            mlist.add(chatMessage);
            madapter.notifyDataSetChanged();
            et_print.setText("");
            olddateTime=newdateTime;
           // openDb();//打开私有数据库
            //getData(outmsg);

        }
        else{
            //Log.i("time","一分钟内");
            ChatMessage chatMessage1 = new ChatMessage();
            chatMessage1.setMsg(outmg);
            chatMessage1.setType(ChatMessage.Type.OUT);
            mlist.add(chatMessage1);
            madapter.notifyDataSetChanged();
            et_print.setText("");
            olddateTime=newdateTime;
           // getData(outmsg);


        }
    }


    public void getMsg(String msg){
        msg=username;
    }

    private void getData(String outmsg) {
        String requestjson="{\n" +
                "                \"key\":\"855b2f1b0f1a4bb89967b812b415abe4\",\n" +
                "                \"info\": \""+outmsg+"\",\n" +
                "                \"userid\":\"123456\"\n" +
                "}" ;
        HttpUtils httpUtils = HttpUtils.getinstance();
        httpUtils.getData("http://www.tuling123.com/openapi/api", requestjson, new HttpResponse<MessageResult>(MessageResult.class) {
            @Override
            public void onSuccess(MessageResult mr) {
                int code = mr.getCode();
                String text = mr.getText();
                Log.i("test",text);
                // Log.i("test",code+"");

                Message msg=mhandler.obtainMessage(0);
               // msg.arg1=0;
                msg.obj=text;
                mhandler.sendMessage(msg);
            }

            @Override
            public void onFailure(String msg) {
                Log.i("test","请求数据失败");
            }
        });

    }
    static class MyHandler extends Handler{
        WeakReference<MainActivity> weak_activity;

        public MyHandler(MainActivity mactivity) {
            this.weak_activity = new WeakReference(mactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity =weak_activity.get();
            switch (msg.what){
                case 0:
                    //回复消息时间判断

                        String text = (String) msg.obj;
                        mainActivity.dao.insert("客服",text,"","IN");
                        //Log.i("test","success");
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setMsg(text);
                        //chatMessage.setDate(new Date());
                        chatMessage.setType(ChatMessage.Type.IN);
                        mainActivity.mlist.add(chatMessage);
                        mainActivity.madapter.notifyDataSetChanged();
                        break;



            }
        }
    }
}
