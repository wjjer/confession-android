package vip.ablog.confession.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant


class WebModuleDetailActivity : AppCompatActivity() {

    private lateinit var wbModuleDetail: WebView;

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_web_module_detail)
        var previewUrl = intent.getStringExtra(Constant.PREVIEW_URL)
        wbModuleDetail = findViewById<WebView>(R.id.wb_module_detail)
        wbModuleDetail.settings.javaScriptEnabled = true
        wbModuleDetail.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        wbModuleDetail.settings.loadWithOverviewMode = true
        wbModuleDetail.settings.mediaPlaybackRequiresUserGesture = false;

        wbModuleDetail.loadUrl(previewUrl);
        wbModuleDetail.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        };
        wbModuleDetail.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                builder.setTitle("")
                    .setMessage(message)
                    .setPositiveButton(
                        "确定",
                        DialogInterface.OnClickListener { dialog, which -> result.confirm() })
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }

            override fun onJsConfirm(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                builder.setTitle("")
                    .setMessage(message)
                    .setPositiveButton(
                        "确定",
                        DialogInterface.OnClickListener { dialog, which -> result.confirm() })
                    .setNegativeButton(
                        "取消",
                        DialogInterface.OnClickListener { dialog, which -> result.cancel() })
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }

            override fun onJsPrompt(
                view: WebView,
                url: String,
                message: String,
                defaultValue: String,
                result: JsPromptResult
            ): Boolean {
                val et = EditText(view.context)
                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                builder.setTitle("")
                    .setMessage(message)
                    .setView(et)
                    .setPositiveButton(
                        "确定",
                        DialogInterface.OnClickListener { dialog, which -> result.confirm(et.text.toString()) })
                    .setNegativeButton(
                        "取消",
                        DialogInterface.OnClickListener { dialog, which -> result.cancel() })
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }
        })


    }

    override fun onDestroy() {
        wbModuleDetail.destroy()
        super.onDestroy()
    }
}