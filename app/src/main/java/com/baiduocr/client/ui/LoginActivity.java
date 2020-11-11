package com.baiduocr.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baiduocr.client.Constans;
import com.baiduocr.client.R;
import com.baiduocr.client.db.DBHelper;
import com.baiduocr.client.utils.SPUtil;
import com.baiduocr.client.utils.StringUtil;

/**
 * class desc :
 */
public class LoginActivity extends BaseActivity {

    private TextView tvForgetPwd;
    private EditText etAccount;
    private EditText etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvForgetPwd = findViewById(R.id.tvForgetPwd);
        etAccount = findViewById(R.id.etAccount);
        etPwd = findViewById(R.id.etPwd);

        setToolBar("图像文字识别助手", false);

        findViewById(R.id.submitLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccount();
            }
        });

        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        findViewById(R.id.register).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void checkAccount() {
        String account = etAccount.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.isPhoneNum(account)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "密码长度不得低于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        loginOrRegister(account, password);
    }

    private void loginOrRegister(String account, String password) {
        String token = DBHelper.userLogin(account, password);
        if (!token.isEmpty()) {
            SPUtil.putString(this, Constans.SP.USER_TOKEN, token);
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
