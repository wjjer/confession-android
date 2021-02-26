package vip.ablog.confession.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class  UploadServerUtils{
    static String uploadUrl = "http://192.168.31.102:8080/upload";
    // 使用OkHttp上传文件
    public static void uploadFile(File file, String id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",id)
                .addFormDataPart("upload", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))//文件
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl) // 上传地址
                .post(requestBody)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                // 文件上传成功
                if (response.isSuccessful()) {
                    Log.i("Haoxueren", "onResponse: " + response.body().string());
                } else {
                    Log.i("Haoxueren", "onResponse: " + response.message());
                }
            }
            @Override
            public void onFailure(@NotNull okhttp3.Call call, IOException e) {
                // 文件上传失败
                Log.i("Haoxueren", "onFailure: " + e.getMessage());
            }
        });
    }
}