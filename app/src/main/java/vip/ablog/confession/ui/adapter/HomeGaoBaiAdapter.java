package vip.ablog.confession.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vip.ablog.confession.R;
import vip.ablog.confession.global.Constant;
import vip.ablog.confession.global.RetMsg;
import vip.ablog.confession.ui.model.ModuleItemBean;
import vip.ablog.confession.utils.DateUtils;
import vip.ablog.confession.utils.FileUtils;
import vip.ablog.confession.utils.HttpUtils;
import vip.ablog.confession.utils.SPUtils;
import vip.ablog.confession.utils.ToastUtils;
import vip.ablog.confession.utils.ZXingUtils;
import vip.ablog.confession.utils.ZipUtils;
import vip.ablog.confession.view.SharePopDialog;

public class HomeGaoBaiAdapter extends BaseQuickAdapter<ModuleItemBean, BaseViewHolder> {

    private Context context;
    private Dialog dialog;
    private Gson gson = new Gson();

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public HomeGaoBaiAdapter(List<ModuleItemBean> list, Context context) {
        super(R.layout.item_fragment_module_gaobai, list);
        this.context = context;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull ModuleItemBean item) {
        helper.setText(R.id.tv_fragment_gaobai_name, item.getName());
        //ImageView view = (ImageView)helper.getView(R.id.iv_fragment_gaobai_cover);
        //view.setImageURI(Uri.fromFile(new File(item.getCover())));
        helper.getView(R.id.btn_fragment_gaobai_mgr).setOnClickListener(v -> {
            //先获取信息
            if (!initLoginInfo()) {
                ToastUtils.showToast(context, "分享告白需要登录！", 0);
                return;
            }
            initDialog();
            RequestBody requestBody = new FormBody.Builder()
                    .add("userConfession.uid", SPUtils.getInstance().getString(context, Constant.CURRENT_USER))
                    .add("userConfession.name", item.getName())
                    .build();
            HttpUtils.sendOkHttpResponse(Constant.SERVER_URL + "confession/getConfession",
                    requestBody, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            ((Activity) context).runOnUiThread(() -> {
                                ToastUtils.showToast(context, "分享失败，请检查网络！", 0);
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String data = response.body().string();
                            // 文件上传成功
                            if (response.isSuccessful()) {
                                RetMsg retMsg = gson.fromJson(data, RetMsg.class);
                                String url = (String) retMsg.getBean();
                                if (!"".equals(url) && url != null) {
                                    showDialog(url);
                                } else {
                                    try {
                                        deployPage(item);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                ((Activity) context).runOnUiThread(() -> {
                                    ToastUtils.showToast(context, "分享失败" + data, 0);
                                });
                            }
                        }
                    });

        });
        helper.getView(R.id.btn_fragment_gaobai_del).setOnLongClickListener(v -> {
            FileUtils.deleteFiles(new File(Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + item.getName()));
            ToastUtils.showToast(context, "删除成功!", 0);
            helper.setGone(helper.itemView.getId(), true);
            return true;

        });

    }

    private boolean initLoginInfo() {
        return SPUtils.getInstance().getBoolean(context, Constant.ISLOGIN);

    }


    private void deployPage(ModuleItemBean item) throws IOException {

        int deployTimes = SPUtils.getInstance().getInt(context, DateUtils.getToday());
        SPUtils.getInstance().putInt(context, DateUtils.getToday(), ++deployTimes);
        if (deployTimes >= Constant.DEPLOY_TIMES_LIMIT) {
            ((Activity) context).runOnUiThread(() -> {
                dialog.dismiss();
                ToastUtils.showToast(context, "每天最多只能部署" + Constant.DEPLOY_TIMES_LIMIT + "次", 1);
            });
            return;
        }

        String id = SPUtils.getInstance().getString(context, Constant.CURRENT_USER);

        String path =
                Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + item.getName();
        ZipUtils.zip(path, path + ".zip", false);
        File file = new File(path + ".zip");
        double dsize = (double) file.length() / 1048576;
        System.out.println("文件大小:" + dsize);
        if (dsize > 6) {
            ((Activity) context).runOnUiThread(() -> {
                ToastUtils.showToast(context, "文件不能超过6M", 1);
                dialog.dismiss();
            });
        } else {
            uploadFile(file, id);
        }

    }

    public void uploadFile(File file, String id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", id)
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))//文件
                .build();
        Request request = new Request.Builder()
                .url(Constant.SERVER_URL + "upload/uploadConfession") // 上传地址
                .post(requestBody)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                // 文件上传成功
                String result = response.body().string();
                RetMsg retMsg = gson.fromJson(result, RetMsg.class);
                if (response.isSuccessful()) {
                    if (RetMsg.MsgType.SUCCESS.equals(retMsg.getType())) {
                        showDialog((String) retMsg.getBean());
                    } else {
                        ((Activity) context).runOnUiThread(() -> {
                            dialog.cancel();
                            ToastUtils.showToast(context, (String) retMsg.getMsg(), 1);
                        });
                    }
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        dialog.cancel();
                        ToastUtils.showToast(context, "部署失败：" + result, 1);
                    });
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, IOException e) {
                // 文件上传失败
                Log.i("Haoxueren", "onFailure: " + e.getMessage());
            }
        });
    }

    void initDialog() {
        dialog = new Dialog(context, R.style.loading);
        dialog.setContentView(R.layout.layout_progress_loading);//此处布局为一个progressbar
        dialog.setCancelable(false); // 可以取消
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = -1;
        params.width = -1;
        params.format = 1;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        dialog.getWindow().setAttributes(params);
    }

    void showDialog(String result) {
        ((Activity) context).runOnUiThread(() -> {
            dialog.cancel();
            SharePopDialog popDialog = new SharePopDialog();
            String data = Constant.SERVER_HOST + result;
            popDialog.showDialog((Activity) context,
                    ZXingUtils.createQRImage(data, 500, 500));
            ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clip == null) {
                ToastUtils.showToast(context, "复制失败，请手动复制", 1);
            } else {
                clip.setText(data); // 复制
            }
        });
    }


}