package vip.ablog.confession;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.yanzhenjie.permission.AndPermission;

import java.io.File;

import vip.ablog.confession.global.Constant;
import vip.ablog.confession.utils.CopyFileFromAssets;
import vip.ablog.confession.utils.ToastUtils;
import vip.ablog.confession.utils.ZipUtil;


public class SplashActivity extends AppCompatActivity {


    private Thread myThread;
    private boolean hasPermission = false;
    private boolean hasNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
           this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //TextView tv = findViewById(R.id.tv_id);
        //tv.setText(id);
        new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    while (!hasPermission) {
                        sleep(500);
                    }
                    initResource();
                    sleep(3000);//使程序休眠一秒
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();//启动线程
        getPermission();


    }

    private void getPermission() {

        String[] permissionArr = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        AndPermission.with(this)
                .permission(permissionArr)
                // 准备方法，和 okhttp 的拦截器一样，在请求权限之前先运行改方法，已经拥有权限不会触发该方法
                .rationale((context, permissions, executor) -> {
                    // 此处可以选择显示提示弹窗
                    executor.execute();
                })
                // 用户给权限了
                .onGranted(permissions -> {
                    hasPermission = true;
                    //ToastUtils.showToast(this,"有储存权限",1);

                })
                // 用户拒绝权限，包括不再显示权限弹窗也在此列
                .onDenied(permissions -> {
                    // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                    if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, permissions)) {
                        // 打开权限设置页
                        AndPermission.permissionSetting(SplashActivity.this).execute();
                        return;
                    }
                    ToastUtils.showToast(this, "没有储存权限，没法告白呢", 1);
                }).start();

    }


    void initResource() {
        initDir();
        if (hasNext) {
            return;
        }
    /*    InputStream inputStream = null;
        InputStream inputStream1 = null;
        InputStream inputStream2 = null;
        OutputStream outputStream = null;
        OutputStream outputStream1 = null;
        OutputStream outputStream2 = null;
        AssetManager assetManager = this.getAssets();


        String targetPath = Constant.APP_BASE_DIR;
        String targetFtlPath = Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE;
        String targetFtlPath2 = Constant.APP_BASE_DIR;*/

        String targetPath = Constant.APP_BASE_DIR + File.separator + "template.zip";
        String targetFtlPath = Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + "index.ftl";
        String targetFtlPath2 = Constant.APP_BASE_DIR + File.separator + "jquery.min.js";
        CopyFileFromAssets.copyFile(this, "template.zip", targetPath);
        CopyFileFromAssets.copyFile(this, "index.ftl", targetFtlPath);
        CopyFileFromAssets.copyFile(this, "jquery.min.js", targetFtlPath2);

        /*    inputStream = getResources().getAssets().open("template.zip");
            inputStream1 = getResources().getAssets().open("index.ftl");
            inputStream2 = getResources().getAssets().open("jquery.min.js");
            outputStream = new FileOutputStream(targetPath);
            outputStream1 = new FileOutputStream(targetFtlPath);
            outputStream2 = new FileOutputStream(targetFtlPath2);
            FileUtils.copy(inputStream, outputStream);
            FileUtils.copy(inputStream1, outputStream1);
            FileUtils.copy(inputStream2, outputStream2);*/
        ZipUtil.upZipFile(targetPath, Constant.APP_BASE_DIR+File.separator);


    }

    void initDir() {
        String appDir = Constant.APP_BASE_DIR;
        File templatePath = new File(appDir + Constant.BASE_DIR_TEMPLATE);
        File pagePath = new File(appDir + Constant.BASE_DIR_PAGE);
        if (!templatePath.exists()) {
            templatePath.mkdirs();
        } else {
            if(templatePath.listFiles().length < 4){
                hasNext = false;
            }else {
                hasNext = true;
            }
        }
        if (!pagePath.exists()) {
            pagePath.mkdirs();
        }

    }

}