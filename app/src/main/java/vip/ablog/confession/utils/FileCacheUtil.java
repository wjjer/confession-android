package vip.ablog.confession.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import static android.content.Context.MODE_APPEND;

/**
 * 描述:
 * <p>
 * Created by allens on 2017/12/26.
 */

public class FileCacheUtil {

    private Context context;
    private String mFileName = "message.txt";

    private FileCacheUtil(Context context) {
        this.context = context;
    }

    private static FileCacheUtil fileCacheUtil;

    public static FileCacheUtil getInstance(Context context) {

        if (fileCacheUtil == null) {
            synchronized (FileCacheUtil.class) {
                if (fileCacheUtil == null) {
                    fileCacheUtil = new FileCacheUtil(context);
                }
            }
        }
        return fileCacheUtil;
    }


    public String read() {
        try {
            FileInputStream inStream = context.openFileInput(mFileName);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            inStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String msg) {
        if (msg == null) {
            return;
        }
        try {
            FileOutputStream fos = context.openFileOutput(mFileName, MODE_APPEND);
            fos.write(msg.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}