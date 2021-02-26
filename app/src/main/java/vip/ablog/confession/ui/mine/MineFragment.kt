package vip.ablog.confession.ui.mine

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.*
import vip.ablog.confession.LoginActivity
import vip.ablog.confession.R
import vip.ablog.confession.UserInfo
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.model.SystemParamBean
import vip.ablog.confession.utils.*
import java.io.IOException

class MineFragment : Fragment() {

    private lateinit var tv_version: TextView
    private lateinit var tv_notify: TextView
    private lateinit var ll_add_group: LinearLayout
    private lateinit var ll_share: LinearLayout
    private lateinit var ll_website: LinearLayout
    private lateinit var ll_dev_id: LinearLayout
    private lateinit var ll_login: LinearLayout
    private lateinit var tv_user: TextView

    var gson: Gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_mine, container, false)
        tv_version = root.findViewById<TextView>(R.id.tv_version)
        ll_add_group = root.findViewById(R.id.ll_add_group)
        tv_notify = root.findViewById(R.id.tv_notify)
        ll_website = root.findViewById(R.id.ll_website)
        ll_dev_id = root.findViewById(R.id.ll_dev_id)
        tv_version.setText("版本号" + SystemUtil.getAppVersionName(activity))
        tv_user = root.findViewById(R.id.tv_user)
        ll_share = root.findViewById(R.id.ll_share)
        ll_login = root.findViewById(R.id.ll_login)
        ll_add_group.setOnClickListener { joinQQGroup("Dpup7W_V497NUIUIWnZ6S9ql8wuYcqap") }
        ll_share.setOnClickListener {
            ShareUtil.shareText(
                activity, "问世间情为何物，直叫人生死相许。问情人花开花落，是造化羽扇纶巾。赶紧下载告白APP给自己喜欢的ta告白吧！下载地址" +
                        Constant.DOWNLOAD_URL, "窈窕淑女，君子好逑"
            )
        }
        ll_website.setOnClickListener { SystemUtil.openBrowser(activity, Constant.WEBSITE) }
        ll_dev_id.setOnClickListener {
            val id = Constant.getId(activity)
            if (id.isBlank()) {
                ToastUtils.showToast(activity, "未获取到设备id，请加群联系管理员！", 1)
            } else {
                val clip =
                    context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clip.text = id
                ToastUtils.showToast(activity, "设备id已复制", 1)
            }

        }
        ll_login.setOnClickListener {
            var login: Boolean = false
            activity?.let {
                login = SPUtils.getInstance().getBoolean(it, Constant.ISLOGIN)
            }
            if (login) {
                startActivity(Intent(activity, UserInfo::class.java))
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }

        }
        getNotify()
        return root
    }

    override fun onResume() {
        super.onResume()
        val userName = activity?.let { SPUtils.getInstance().getString(it, Constant.CURRENT_USER) }
        if (userName != null && !"".equals(userName)) {
            tv_user?.text = userName
        } else {
            tv_user?.text = "注册登录"
        }
    }

    private fun getNotify() {
        /*val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("key", Constant.NOTIFY)
            .build()*/
        val requestBody = FormBody.Builder()
            .add("key", Constant.NOTIFY).build()
        HttpUtils.sendOkHttpResponse(
            Constant.SERVER_URL + "param/getParamByKey",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    var data = response.body!!.string()
                    // 文件上传成功
                    if (response.isSuccessful) {
                        val param: SystemParamBean =
                            gson.fromJson(data, SystemParamBean::class.java)
                        activity?.runOnUiThread(Runnable {
                            tv_notify.setText(param.value)
                        })
                    }
                }
            })
    }


    /****************
     *
     * 发起添加群流程。群号：IT Studio(524115760) 的 key 为： Dpup7W_V497NUIUIWnZ6S9ql8wuYcqap
     * 调用 joinQQGroup(Dpup7W_V497NUIUIWnZ6S9ql8wuYcqap) 即可发起手Q客户端申请加群 IT Studio(524115760)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            return false;
        }

    }


}