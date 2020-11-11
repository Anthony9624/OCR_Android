package com.baiduocr.client;

import android.util.Log;

import com.baiduocr.client.db.DBHelper;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * class desc :
 */
public class App extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        initDB();
    }

    private void initDB() {
        String DB_DIR_PATH = getFilesDir() + "/databases";
        DBHelper.DB_PATH = DB_DIR_PATH + "/ticket.db";
        // 初始化数据库
        if (!new File(DB_DIR_PATH + "/ticket.db").exists()) {
            try {
                new File(DB_DIR_PATH).mkdir();
                FileOutputStream fos = new FileOutputStream(DB_DIR_PATH + "/ticket.db");
                InputStream is = getAssets().open("ticket.db");
                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                Log.v("LOG", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
