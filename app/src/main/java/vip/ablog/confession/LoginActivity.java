package vip.ablog.confession;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import vip.ablog.confession.global.Constant;
import vip.ablog.confession.global.RetMsg;
import vip.ablog.confession.utils.DeviceIdUtils;
import vip.ablog.confession.utils.HttpUtils;
import vip.ablog.confession.utils.SPUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btn_resg, btn_login;
    private Handler handler;
    private String TAG = "LoginActivity";
    TextView  tv_devid;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        tv_devid = findViewById(R.id.tv_devid);
        btn_resg = (Button) findViewById(R.id.btn_resg);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        btn_resg.setOnClickListener(this);
        mPasswordView = (EditText) findViewById(R.id.password);
        handler = new MyHandler();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        tv_devid.setText(DeviceIdUtils.getDeviceId(this));
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> attemptLogin());
    }


    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!TextUtils.isEmpty(email) && !isPasswordValid(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            startLogin();

        }
    }

    //开始登录
    private void startLogin() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String url = Constant.SERVER_URL + "user/login";
        RequestBody requestBody = new FormBody.Builder()
                .add("sysUser.email", email)
                .add("sysUser.passwd", password)
                .build();
        HttpUtils.sendOkHttpResponse(url,
                requestBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        handler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String data = response.body().string();
                        if (response.isSuccessful()) {
                            RetMsg retMsg = gson.fromJson(data, RetMsg.class);
                            if (RetMsg.MsgType.SUCCESS.equals(retMsg.getType())) {
                                //SysUser userInfo = (SysUser) retMsg.getBean();
                                handler.sendEmptyMessage(4);  //登录成功
                                SPUtils.getInstance().putString(LoginActivity.this, Constant.CURRENT_USER, email);
                                SPUtils.getInstance().putBoolean(LoginActivity.this, Constant.ISLOGIN, true);
                                finish();
                            } else {
                                handler.sendEmptyMessage(3);
                            }
                        }
                    }

                });
    }


    //开始注册


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        if (email.equals("") || password.equals("") || password.length() < 6 || email.length() < 6) {
            Toast.makeText(this, "邮箱和密码长度必须大于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgress(true);
        String url = Constant.SERVER_URL + "user/register";

        RequestBody requestBody = new FormBody.Builder()
                .add("sysUser.email", email)
                .add("sysUser.devid", tv_devid.getText().toString())
                .add("sysUser.passwd", password)
                .build();
        HttpUtils.sendOkHttpResponse(url,
                requestBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        handler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String data = response.body().string();
                        if (response.isSuccessful()) {
                            RetMsg retMsg = gson.fromJson(data, RetMsg.class);
                            if (RetMsg.MsgType.SUCCESS.equals(retMsg.getType())) {
                                handler.sendEmptyMessage(1);  //登录成功
                            } else {
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }
                });


    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this, "该邮箱已经注册！", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "网络请求异常！", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "用户未注册或密码错误！", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                    showProgress(false);

                    break;
                case 5:
                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;


            }
        }

    }

}

