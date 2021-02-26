package vip.ablog.confession.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class CopyFileFromAssets {
    public static void copyFile(Context context, String filename, String target) {
        InputStream in = null;
        FileOutputStream out = null; // path为指定目录
        //String path = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + filename; // data/data目录
        File file = new File(target);
        if (!file.exists()) {
            try {
                in = context.getAssets().open(filename); // 从assets目录下复制
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

}