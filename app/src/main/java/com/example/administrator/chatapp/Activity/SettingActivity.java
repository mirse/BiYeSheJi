package com.example.administrator.chatapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.chatapp.MainActivity;
import com.example.administrator.chatapp.R;
import com.example.administrator.chatapp.Utils.Constant;
import com.example.administrator.chatapp.Utils.Event;
import com.example.administrator.chatapp.Utils.SharedPrefsUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/21.
 */

public class SettingActivity extends Activity implements View.OnClickListener {

    private Button bt_headimg;
    private Button bt_save;
    private CircleImageView civ_headimg;
    public static final int CHOOSE_PHOTO=0;
    public static final int SHOW_PGOTO=1;
    public static final int TAKE_PHOTO=2;
    public static final int CROP_PHOTO=3;

    private View rootview;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private PopupWindow popupWindow;
    private Uri imguri;
    private File file;
    private EditText et_username;
    private Bitmap photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();//获取到了当前界面的DecorView,通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initUI();
    }

    private void initUI() {
        String defusername = SharedPrefsUtil.getStringValue(getApplicationContext(), Constant.USERNAME, "");

        //设置默认头像
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.defuserimg);
        Bitmap bitmap1 = SharedPrefsUtil.getBitmap(getApplicationContext(), Constant.BITMAP, bitmap);

        et_username = findViewById(R.id.et_username);
        et_username.setText(defusername);
        bt_headimg = findViewById(R.id.bt_headimg);
        bt_save = findViewById(R.id.bt_save);
        civ_headimg = findViewById(R.id.civ_headimg);
        findViewById(R.id.setting_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        civ_headimg.setImageBitmap(bitmap1);
        bt_headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();

            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                SharedPrefsUtil.putStringValue(getApplicationContext(), Constant.USERNAME,username);
                SharedPrefsUtil.putBitmap(getApplicationContext(),Constant.BITMAP,photo);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();

                Intent intent=new Intent();
                intent.putExtra("PHOTO",datas);
                intent.putExtra("UN",username);
                setResult(Activity.RESULT_OK,intent);
               // EventBus.getDefault().post(new Event(username));
                Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    /**
     * 显示照片选择框
     */
    private void showPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.selectphoto_dialog, null,false);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        rootview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_slidemenu, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM,0,0);
        popupWindow.setContentView(view);
        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        // pop_layout=(LinearLayout)findViewById(R.id.pop_layout);
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    cropPhoto(data.getData());
                }

                break;
            case SHOW_PGOTO:
               if (data!=null){
                   Bundle extras = data.getExtras();
                   photo = extras.getParcelable("data");
                   civ_headimg.setImageBitmap(photo);
                   popupWindow.dismiss();
               }
                break;
            case TAKE_PHOTO:
                //imguri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                //cropPhoto(imguri);
                photo = (Bitmap) data.getExtras().get("data");
                civ_headimg.setImageBitmap(photo);
                popupWindow.dismiss();
                break;
        }
    }

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SHOW_PGOTO);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pick_photo:
                Intent intent =new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,CHOOSE_PHOTO);
                break;
            case R.id.btn_take_photo:
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
//                Date date = new Date();
//                String filename = simpleDateFormat.format(date);
//                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                file = new File(path, filename + ".jpg");
//                try {
//                    if (file.exists()){
//                        file.delete();
//                    }
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imguri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent1=new Intent("android.media.action.IMAGE_CAPTURE");
//                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
                startActivityForResult(intent1,TAKE_PHOTO);

                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
        }
    }
}
