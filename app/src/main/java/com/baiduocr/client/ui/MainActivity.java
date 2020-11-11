package com.baiduocr.client.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baiduocr.client.ActivityController;
import com.baiduocr.client.Constans;
import com.baiduocr.client.R;
import com.baiduocr.client.db.DBHelper;
import com.baiduocr.client.utils.SPUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_GENERAL_BASIC = 106;

    private boolean hasGotToken = false;
    private EditText etText;
    private StringBuilder resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.et_result);

        findViewById(R.id.btn).setOnClickListener(v -> {
            if (!checkTokenStatus()) {
                return;
            }
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
        });

        findViewById(R.id.tv_copy).setOnClickListener(v -> {
            if (resultData != null) {
                Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", resultData);
                if (cm != null)
                    cm.setPrimaryClip(mClipData);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            showLogoutDialog();
        });

        findViewById(R.id.btn_old).setOnClickListener(v -> {
            startActivity(new Intent(this, OldListActivity.class));
        });

        initAccessTokenWithAkSk();
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("确认退出登录吗？");
        alertDialog.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        alertDialog.setPositiveButton("退出登录", (dialog, which) -> {
            SPUtil.putString(MainActivity.this, Constans.SP.USER_TOKEN, "");
            ActivityController.finishAll();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            dialog.dismiss();
        });
        alertDialog.show();
    }

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getApplicationContext(), "UZhpuOVdVKT67DH0GioK81sy", "yeO4KgV7qeqVYGoGBkEpPATfj4a9FAvU");
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            Gson gson = new Gson();
            RecognizeService.recGeneralBasic(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                    result -> {
                        Log.i(TAG, "onActivityResult: " + result);
                        Result mData = gson.fromJson(result, Result.class);
                        if (mData != null) {
                            List<Result.Data> words_result = mData.words_result;
                            resultData = new StringBuilder();
                            if (words_result != null) {
                                for (Result.Data data1 : words_result) {
                                    resultData.append(data1.words);
                                }
                            }
                            if (!TextUtils.isEmpty(resultData)) {
                                DBHelper.addOld(resultData.toString());
                                etText.setText(resultData);
                            } else {
                                Toast.makeText(this, "识别结果为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    static class Result {
        private List<Data> words_result;

        public List<Data> getWords_result() {
            return words_result;
        }

        static class Data {
            private String words;

            public String getWords() {
                return words;
            }
        }
    }
}
