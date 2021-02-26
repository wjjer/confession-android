package vip.ablog.confession;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import vip.ablog.confession.global.Constant;
import vip.ablog.confession.utils.SPUtils;

public class UserInfo extends AppCompatActivity {

    TextView tv_user_current;
    Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        tv_user_current = findViewById(R.id.tv_user_current);
        String user = SPUtils.getInstance().getString(this, Constant.CURRENT_USER);
        btn_logout = findViewById(R.id.btn_logout);

        tv_user_current.setText(user);
        btn_logout.setOnClickListener(v->{
            SPUtils.getInstance().remove(this,Constant.CURRENT_USER);
            SPUtils.getInstance().remove(this,Constant.ISLOGIN);
            finish();
        });
    }
}