package vip.ablog.confession

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.*
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.model.SystemParamBean
import vip.ablog.confession.utils.HttpUtils
import vip.ablog.confession.utils.SystemUtil
import java.io.IOException
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {

    lateinit var dialogUpdate:AlertDialog
    lateinit var dialogLimit:AlertDialog
    var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        initTestVersion()
        initversion()
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        with(navView) {
            setupActionBarWithNavController(navController, appBarConfiguration)
            setupWithNavController(navController)
        }

    }

    private fun initversion() {
        val requestBody = FormBody.Builder()
            .add("key", Constant.UPDATE).build()
        HttpUtils.sendOkHttpResponse(
            Constant.SERVER_URL + "param/getParamByKey",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }
                override fun onResponse(call: Call, response: Response) {
                    var data = response.body!!.string()
                    if (response.isSuccessful) {
                        val appVersionCode = SystemUtil.getAppVersionCode(this@MainActivity)
                        val param: SystemParamBean =
                            gson.fromJson(data, SystemParamBean::class.java)
                        if (appVersionCode != param.value.toLong()) {
                            this@MainActivity.runOnUiThread(Runnable {
                                val alertdialogbuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(this@MainActivity)
                                alertdialogbuilder.setMessage("获取到更新\n\n" + param.info)
                                alertdialogbuilder.setPositiveButton("退出") { dialogInterface, pos ->
                                    this@MainActivity.finish()

                                }
                                alertdialogbuilder.setNeutralButton("更新") { dialogInterface, pos ->
                                    val field: Field = dialogUpdate.javaClass.superclass!!.getDeclaredField("mShowing")
                                    field.isAccessible = true
                                    field.set(dialogUpdate, false) //dialog点击后不会消失

                                    val durl = param.status
                                    SystemUtil.openBrowser(this@MainActivity, durl)
                                }
                                dialogUpdate = alertdialogbuilder.create()
                                dialogUpdate.setCanceledOnTouchOutside(false)
                                dialogUpdate.setCancelable(false)
                                dialogUpdate.show()
                            })
                        }


                    }
                }
            })
    }


    private fun initTestVersion() {
        val requestBody = FormBody.Builder()
            .add("key", Constant.TEST).build()
        HttpUtils.sendOkHttpResponse(
            Constant.SERVER_URL + "param/getParamByKey",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }
                override fun onResponse(call: Call, response: Response) {
                    var data = response.body!!.string()
                    if (response.isSuccessful) {
                        val param: SystemParamBean =
                            gson.fromJson(data, SystemParamBean::class.java)
                        if (Constant.TEST_APP .equals(param.value)) {
                            this@MainActivity.runOnUiThread(Runnable {
                                val alertdialogbuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(this@MainActivity)
                                alertdialogbuilder.setMessage(param.name+ "\n\n" + param.info)
                                alertdialogbuilder.setPositiveButton("退出") { dialogInterface, pos ->
                                    this@MainActivity.finish()

                                }
                                alertdialogbuilder.setNeutralButton("确定") { dialogInterface, pos ->
                                    dialogLimit.dismiss()
                                }
                                dialogLimit = alertdialogbuilder.create()
                                dialogLimit.setCanceledOnTouchOutside(false)
                                dialogLimit.setCancelable(false)
                                dialogLimit.show()
                            })
                        }else if(Constant.STOP_APP .equals(param.value)){
                            this@MainActivity.runOnUiThread(Runnable {
                                val alertdialogbuilder: AlertDialog.Builder =
                                    AlertDialog.Builder(this@MainActivity)
                                alertdialogbuilder.setMessage(param.name+ "\n\n" + param.info)
                                alertdialogbuilder.setPositiveButton("退出") { dialogInterface, pos ->
                                    this@MainActivity.finish()

                                }
                                dialogLimit = alertdialogbuilder.create()
                                dialogLimit.setCanceledOnTouchOutside(false)
                                dialogLimit.setCancelable(false)
                                dialogLimit.show()
                            })
                        }else if(Constant.NORMAL_APP .equals(param.value)){

                        }


                    }
                }
            })
    }



}