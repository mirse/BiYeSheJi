package com.example.administrator.chatapp.Activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.chatapp.R;

/**
 * Created by Administrator on 2018/1/29.
 */

public class AboutActivity extends Activity {

    private ImageView iv_about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initUi();
    }

    private void initUi() {
        iv_about = findViewById(R.id.iv_about);
        findViewById(R.id.about_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.about_anim);
        iv_about.setBackgroundDrawable(drawable);
        drawable.start();

    }
}
