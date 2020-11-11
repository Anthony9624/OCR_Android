package com.baiduocr.client.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class desc :
 *
 * @author :
 */
public class StringUtil {

    public static boolean isPhoneNum(String phoneNum) {
        Pattern p = Pattern.compile("^[1][3-9]\\d{9}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    public static void setClipboard(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(mClipData);
    }
}
