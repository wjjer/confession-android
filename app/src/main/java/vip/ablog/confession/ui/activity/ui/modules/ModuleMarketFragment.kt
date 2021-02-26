package vip.ablog.confession.ui.activity.ui.modules

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.activity.WebModuleDetailActivity
import vip.ablog.confession.ui.activity.ui.main.PageViewModel
import vip.ablog.confession.ui.adapter.MarketItemAdapter
import vip.ablog.confession.ui.model.MarketItemBean
import vip.ablog.confession.utils.*
import java.io.File
import java.io.IOException
import java.lang.reflect.Type


/**
 * A placeholder fragment containing a simple view.
 */
class ModuleMarketFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var rv_market: RecyclerView
    lateinit var adapter: MarketItemAdapter
    lateinit var contentView: View
    private var moduleDataList: MutableList<MarketItemBean> = ArrayList()
    var pageNo = 0
    var gson: Gson = Gson()
    var type: Type? = object : TypeToken<List<MarketItemBean>>() {}.type
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progressBar = ProgressBar(activity)
        contentView = inflater.inflate(R.layout.fragment_module_market, container, false)
        rv_market = contentView.findViewById<RecyclerView>(R.id.rv_fragment_module_market)
        adapter = MarketItemAdapter(moduleDataList, activity)
        adapter.addFooterView(progressBar)
        initData();
        initListener();
        return contentView
    }

    private fun initListener() {
        rv_market?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                var layoutManager: LinearLayoutManager? =
                    recyclerView.layoutManager as LinearLayoutManager?
                var firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleItemPosition == 0) {
                    //activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                }
                var lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager?.getItemCount()!! - 1) {
                    activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                    //加载更多
                    initData()
                }
            }
        })
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val alertdialogbuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                alertdialogbuilder.setMessage("详情\n\n" + moduleDataList.get(position).info)
                alertdialogbuilder.setPositiveButton("预览") { dialogInterface, pos ->
                    var intent = Intent()
                    activity?.let {
                        intent.setClass(it, WebModuleDetailActivity::class.java)
                        intent.putExtra(Constant.PREVIEW_URL, moduleDataList[position].purl)
                    }
                    startActivity(intent)

                }
                alertdialogbuilder.setNeutralButton("下载") { dialogInterface, pos ->

                    val durl = moduleDataList.get(position).durl
                    //DownloadUtils.downloadAPK(activity, durl,moduleDataList.get(position).name,"模板文件",false)
                    DownloadUtil.get().download(durl,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
                        moduleDataList.get(position).name,object :DownloadUtil.OnDownloadListener{
                            override fun onDownloading(progress: Int) {
                                activity?.runOnUiThread(Runnable {
                                    ToastUtils.showToast(activity,"正在现在中",0)
                                })
                            }

                            override fun onDownloadFailed(e: Exception?) {
                                activity?.runOnUiThread(Runnable {
                                    ToastUtils.showToast(activity,"下载失败！",0)
                                })
                            }

                            override fun onDownloadSuccess(file: File?) {

                                activity?.runOnUiThread(Runnable {
                                    //部署模板
                                    val result =
                                        ZipUtil.upZipFile(
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_DOWNLOADS
                                            ).toString() + File.separator + moduleDataList.get(position).name,
                                            Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE+File.separator
                                        )
                                    if (result) {
                                        ToastUtils.showToast(activity, "模板安装成功！", 1)
                                    } else {
                                        ToastUtils.showToast(activity, "模板安装失败！", 1)
                                    }
                                })

                            }

                        }
                    )
                }
                val alertdialog1: AlertDialog = alertdialogbuilder.create()
                alertdialog1.show()
            }

        })
    }

    private fun initData() {
        pageNo++
        val requestBody = FormBody.Builder()
            .add("pageNo", pageNo.toString())
            .add("limit", Constant.PAGE_LIMIT) //文件
            .build()
        HttpUtils.sendOkHttpResponse(
            Constant.SERVER_URL + "template/findTemplateList",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    activity?.runOnUiThread(Runnable() {
                        ToastUtils.showToast(context, "请稍后再试", 0)
                    })
                }

                override fun onResponse(call: Call, response: Response) {

                    var data = response.body!!.string()
                    // 文件上传成功
                    if (response.isSuccessful) {
                        Log.i("Haoxueren", "onResponse: " + data)
                        if (pageNo == 1) {
                            activity?.runOnUiThread(Runnable() {
                                moduleDataList = gson.fromJson(data, type)
                                if (moduleDataList == null || moduleDataList.size == 0) {
                                    ToastUtils.showToast(context, "暂无更多数据", 0)
                                    return@Runnable
                                }
                                rv_market.adapter = adapter
                                rv_market.layoutManager = LinearLayoutManager(activity)
                                adapter.setNewInstance(moduleDataList)
                            })
                        } else {
                            activity?.runOnUiThread(Runnable() {
                                val moduleDataList: List<MarketItemBean> = gson.fromJson(data, type)
                                adapter.addData(moduleDataList)
                            })
                        }

                    } else {
                        Log.i("Haoxueren", "onResponse: " + response.message)
                    }
                    adapter.removeFooterView(progressBar)

                }
            })


    }

}