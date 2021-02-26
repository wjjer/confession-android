package vip.ablog.confession.ui.activity.ui.modules

import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant
import vip.ablog.confession.utils.*
import java.io.File

class ModuleDataActivity : AppCompatActivity() {

    lateinit var ll_data: LinearLayout
    lateinit var fab_activity_module_data: FloatingActionButton
    lateinit var map: Map<String, EditText>
    lateinit var keyMap: Map<String, String>
    lateinit var moduleName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_data)
        ll_data = findViewById<LinearLayout>(R.id.ll_data)
        supportActionBar?.setTitle("配置模板数据")
        fab_activity_module_data = findViewById<FloatingActionButton>(R.id.fab_activity_module_data)

        initData()

        fab_activity_module_data.setOnClickListener { view ->
            var filePath =
                Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + moduleName
            keyMap = mutableMapOf()
            for (entry in map) {
                var et: EditText = entry.value
                (keyMap as MutableMap<String, String>).put(entry.key, et.text.toString() ?: " ")
            }
            saveTemplateDate(moduleName, keyMap as MutableMap<String, String>)

            var result = FreemarkerUtil.createHtmlFromModel(
                this,
                filePath,
                filePath,
                "index.ftl",
                "index.html",
                keyMap
            )
            if ("0".equals(result)) {
                this.finish()
                Toast.makeText(this, "模板修改成功,点击可以预览！", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveTemplateDate(templateName: String, keyMap: MutableMap<String, String>) {
        val json = JSONObject(keyMap as Map<*, *>)
        SPUtils.getInstance().putString(this, templateName, json.toString())
    }

    private fun initData() {
        var keyList: List<String> = ArrayList()
        moduleName = intent.getStringExtra(Constant.MODULE_NAME)
        var path = intent.getStringExtra(Constant.MODULE_URL)
        try {
            keyList = FileReaderUtil.read(path)
        } catch (e: Exception) {
            ToastUtils.showToast(this, "模板解析错误！", 1)
        }
        var mutableMap = mutableMapOf<String, EditText>()
        for (key in keyList) {
            var l: LinearLayout = LinearLayout(this);
            var params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(10, 10, 5, 10)
            var et = EditText(this)
            et.hint = StrConvertUtil.convert(key)
            mutableMap.put(key, et)
            l.addView(et)
            ll_data.addView(l, params)
        }

        //恢复上次数据
        val data = SPUtils.getInstance().getString(this, moduleName)
        println("已经存储的数据："+data)
        if (data != null && data != "") {
            val json: JSONObject = JSONObject(data)
            val dataMap = toHashMap(json)
            dataMap?.forEach { (k, v) ->
                run {
                    mutableMap.get(k)?.setText(v)
                }
            }
        }
        map = mutableMap

    }

    private fun toHashMap(jsonObject: JSONObject): HashMap<String, String>? {
        val data = HashMap<String, String>()
        // 将json字符串转换成jsonObject
        //val jsonObject: JSONObject = JSONObject.fromObject(`object`)
        val it: Iterator<*> = jsonObject.keys()
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            val key = it.next().toString()
            val value = jsonObject[key] as String
            data[key] = value
        }
        return data
    }

}