package com.baiduocr.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.baiduocr.client.Constans;
import com.baiduocr.client.R;
import com.baiduocr.client.db.DBHelper;
import com.baiduocr.client.utils.SPUtil;

/**
 * class desc :
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String token = SPUtil.getString(SplashActivity.this, Constans.SP.USER_TOKEN, "");
                if (TextUtils.isEmpty(token)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    DBHelper.userFind(token);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }).start();
    }
}
