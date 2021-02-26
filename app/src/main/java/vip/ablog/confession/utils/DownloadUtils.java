package vip.ablog.confession.utils;

import android.app.DownloadManager;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.content.IntentFilter;

import android.database.Cursor;

import android.net.Uri;

import android.os.Build;

import android.os.Environment;


import androidx.core.content.FileProvider;

import java.io.File;

import java.io.IOException;

import vip.ablog.confession.global.Constant;

/**
 * Created by HJS on 2018/4/8.
 */

public class DownloadUtils {

//下载器

    private static DownloadManager downloadManager;

    //下载的ID

    private static long downloadId;
    private static boolean mapk = false;

    private static String apkFileName = "confession.apk";

    private static Context mcontext;

    //广播监听下载的各个状态

    private static BroadcastReceiver receiver = new

            BroadcastReceiver() {

                @Override

                public void onReceive(Context context, Intent intent) {

                    checkStatus();

                }

            };

    //下载apk

    public static void downloadAPK(Context context, String url, String title, String desc, boolean apk) {
        mcontext = context;
        mapk = apk;

        //删除旧包
        if (!apk) {
            apkFileName = title + ".zip";
        }

        File oldApkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkFileName);

        if (oldApkFile.exists()) {

            oldApkFile.delete();

        }


//创建下载任务


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //移动网络情况下是否允许漫游

        request.setAllowedOverRoaming(false);

        //在通知栏中显示，默认就是显示的

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        request.setTitle(title);

        request.setDescription(desc);

        request.setVisibleInDownloadsUi(true);

        request.setMimeType("application/vnd.android.package-archive");

        //设置下载的路径

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkFileName);

        //获取DownloadManager

        downloadManager = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等

        downloadId = downloadManager.enqueue(request);

        //动态注册广播接收者，监听下载状态

        mcontext.registerReceiver(receiver,

                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

//检查下载状态

    private static void checkStatus() {

        DownloadManager.Query query = new DownloadManager.Query();

        //通过下载的id查找

        query.setFilterById(downloadId);

        Cursor c = downloadManager.query(query);

        if (c.moveToFirst()) {

            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

            switch (status) {

//下载暂停

                case DownloadManager.STATUS_PAUSED:

                    break;

                //下载延迟

                case DownloadManager.STATUS_PENDING:

                    break;

                //正在下载

                case DownloadManager.STATUS_RUNNING:

                    break;

                //下载完成

                case DownloadManager.STATUS_SUCCESSFUL:

//下载完成安装APK

                    if (mapk) {
                        installAPK();
                    }else{
                        //部署模板
                        boolean result = ZipUtil.upZipFile( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + apkFileName
                                , Constant.APP_BASE_DIR+Constant.BASE_DIR_TEMPLATE+File.separator);

                        if (result){
                            ToastUtils.showToast(mcontext, "模板安装成功！", 1);
                        }else{
                            ToastUtils.showToast(mcontext, "模板安装失败！", 1);
                        }

                    }

                    break;

                //下载失败

                case DownloadManager.STATUS_FAILED:

                    ToastUtils.showToast(mcontext, "下载失败", 1);

                    break;

            }

        }

        c.close();

    }

    /**
     * 兼容 android 8.0
     */

    private static void installAPK() {

        File apkFile =

                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkFileName);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {

//如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装

            String[] command = {"chmod", "777", apkFile.toString()};

            ProcessBuilder builder = new ProcessBuilder(command);

            builder.start();

            if (Build.VERSION.SDK_INT >= 24) {

                Uri apkUri = FileProvider.getUriForFile(mcontext, mcontext.getPackageName() + ".updateFileProvider", apkFile);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

            } else {

                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

            }

            mcontext.startActivity(intent);

        } catch (IOException ignored) {

            ignored.printStackTrace();

        }

    }

}