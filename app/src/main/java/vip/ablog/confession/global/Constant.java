package vip.ablog.confession.global;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Method;

import vip.ablog.confession.utils.MD5Utils;
import vip.ablog.confession.utils.SDUtils;

public class Constant {

    public static final String SERVER_URL = "";
    public static final String SERVER_HOST = "";
    //  public static final String SERVER_URL = "http://192.168.1.8:80/";
    // public static final String DEV_SERVER_URL = "http://192.168.31.102:8080/";
    public static final String RES_TITLE = "";
    public static final String PREVIEW_URL = "PREVIEW_URL";
    public static final String MODULE_URL = "MODULE_URL";
    public static final String MODULE_NAME = "MODULE_NAME";
    public static final String APP_BASE_DIR = SDUtils.getSDCardRootDir() + File.separator + "chaseLover";
    public static final String BASE_DIR_TEMPLATE = File.separator + "template";
    public static final String BASE_DIR_PAGE = File.separator + "page";
    @NotNull
    public static final String PAGE_LIMIT = "10";
    public static final Integer FREE = 1;
    @NotNull
    public static final String UPDATE = "update";
    public static final String NOTIFY = "notify";
    @NotNull
    public static final String DOWNLOAD_URL = "https://ablog.lanzous.com/b015njgid";
    /*官网*/
    @Nullable
    public static final String WEBSITE = "http://confession.ablog.vip";
    public static final String DEPLOY_TIMES = "DEPLOY_TIMES";
    public static final String CURRENT_USER = "user";
    public static final String ISLOGIN = "isLogin";
    public static final String SUCCESS = "";
    @NotNull
    public static final String TEST = "test";
    @NotNull
    public static final Object TEST_APP = "1";
    public static final Object NORMAL_APP = "2";
    public static final Object STOP_APP = "3";
    public static final int DEPLOY_TIMES_LIMIT = 50;


    public static synchronized String getId(Context ctx) {
        String s = getAndroidId(ctx) + getSerialNumber();
        return MD5Utils.digest(s.getBytes());
    }


    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (context == null) {
            return "";
        }

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return (TextUtils.isEmpty(androidId) ? "" : androidId);
    }

    /**
     * 获取android序列号
     *
     * @return id或者空串
     */
    private static synchronized String getSerialNumber() {
        String serialNumber = null;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            if (clazz != null) {
                Method method_get = clazz.getMethod("get", String.class, String.class);
                if (method_get != null) {
                    serialNumber = (String) (method_get.invoke(clazz, "ro.serialno", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialNumber != null ? serialNumber : "";
    }


}
