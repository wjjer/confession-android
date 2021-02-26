package vip.ablog.confession.ui.activity.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import vip.ablog.confession.R;
import vip.ablog.confession.global.Constant;
import vip.ablog.confession.ui.model.ModuleItemBean;
import vip.ablog.confession.utils.FileReaderUtil;
import vip.ablog.confession.utils.FileUtils;
import vip.ablog.confession.utils.FreemarkerUtil;
import vip.ablog.confession.utils.ToastUtils;

public class GenerateActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog multiDialog; //多选框
    private LinearLayout ll_module;
    private List<Button> btnModuleList;
    private FloatingActionButton fb_generate;
    private EditText et_age, et_nick, et_name, et_module;
    private List<String> checksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        initView();
    }


    void initView() {
        Button select = findViewById(R.id.btn_select);
        ll_module = findViewById(R.id.ll_module);
        fb_generate = findViewById(R.id.fb_generate);
        et_age = findViewById(R.id.et_age);
        et_module = findViewById(R.id.et_module);
        et_name = findViewById(R.id.et_name);
        et_nick = findViewById(R.id.et_nick);
        select.setOnClickListener(this);
        getSupportActionBar().setTitle(R.string.add);
        fb_generate.setOnClickListener(this);

    }

    public void showMutilAlertDialog(View view) {
        List<ModuleItemBean> localStorageModulesList = FileReaderUtil.getLocalStorageModules();
        final String[] items = new String[localStorageModulesList.size()];
        for (int i = 0; i < localStorageModulesList.size(); i++) {
            items[i] = localStorageModulesList.get(i).getName();
        }

        checksList = new ArrayList<>();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择模板 (按选择顺序生成)");
        /**
         *第一个参数:弹出框的消息集合，一般为字符串集合
         * 第二个参数：默认被选中的，布尔类数组
         * 第三个参数：勾选事件监听
         */
        alertBuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    checksList.add(items[i]);
                } else {
                    checksList.remove(items[i]);
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                multiDialog.dismiss();
                //添加\
                if (btnModuleList == null) {
                    btnModuleList = new ArrayList<>();
                } else {
                    btnModuleList.clear();
                }
                if (checksList.size() == 0) {
                    return;
                }
                for (int i = 0; i < checksList.size(); i++) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                    params.setMargins(10, 10, 5, 10);
                    Button button = new Button(GenerateActivity.this);
                    button.setText(checksList.get(i));
                    button.setId(i);
                    button.setOnClickListener(GenerateActivity.this);
                    button.setTextColor(Color.BLACK);
                    button.setBackgroundColor(
                            Color.rgb(
                                    new Random().nextInt(100)+140,
                                    new Random().nextInt(100)+140,
                                    new Random().nextInt(100)+140));
                    ll_module.addView(button, params);
                    btnModuleList.add(button);
                }
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                multiDialog.dismiss();
            }
        });


        multiDialog = alertBuilder.create();
        multiDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_select) {
            showMutilAlertDialog(v);
        } else if (v.getId() == R.id.fb_generate) {
            startGeneratePage();
        } else {
            ll_module.removeView(btnModuleList.get(v.getId()));
        }
    }

    private void startGeneratePage() {
        if (btnModuleList == null || btnModuleList.size() == 0 || checksList == null || checksList.size() == 0) {
            ToastUtils.showToast(this, "请先选择模板！", 1);
            return;
        }
        if (et_module.getText().toString().trim().equals("")) {
            ToastUtils.showToast(this, "告白的名字不能为空！", 0);
            return;
        }
      /*  if (et_name.getText().toString().trim().equals("")) {
            ToastUtils.showToast(this, "被告白者名不能为空！", 0);
            return;
        }*/


        String path = Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE;

        String targePath = Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + et_module.getText().toString().trim();
        String indexPathName = "";
        for (int i = 0; i < checksList.size(); i++) {
            File targetDir = new File(targePath + File.separator + checksList.get(i));
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            indexPathName += "./" + checksList.get(i) + "/index.html,";
            FileUtils.copyFolder(path + File.separator + checksList.get(i), targePath + File.separator + checksList.get(i));
            //System.out.println("复制文件" + i + "成功！！！");
        }


        Map<String, String> mapData = new HashMap<>();
        mapData.put("_urls_", indexPathName);
        String result = FreemarkerUtil.createHtmlFromModel(this,
                Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE,
                Constant.APP_BASE_DIR + Constant.BASE_DIR_PAGE + File.separator + et_module.getText().toString().trim(),
                "index.ftl",
                "index.html",
                mapData);
        if ("0".equals(result)){
            this.finish();
            Toast.makeText(this,"生成告白成功！",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        }
    }
}