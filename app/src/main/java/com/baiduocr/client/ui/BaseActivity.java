package com.baiduocr.client.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baiduocr.client.ActivityController;
import com.baiduocr.client.Constans;
import com.baiduocr.client.R;
import com.baiduocr.client.utils.SPUtil;

/**
 * class desc :
 *
 * @author :
 */
abstract class BaseActivity extends AppCompatActivity {

    protected LoginOutBroadcastReceiver locallReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    private void registerActivityControllerBroadcast() {
        // 注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActivityController.LOGOUT_ACTION);
        locallReceiver = new LoginOutBroadcastReceiver();
        registerReceiver(locallReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerActivityControllerBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locallReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    public class LoginOutBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityController.finishAll();  // 销毁所有活动
            SPUtil.putString(BaseActivity.this, Constans.SP.USER_TOKEN, "");
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    public void setToolBar(String title, boolean canBack) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        if (canBack) {
            this.setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
