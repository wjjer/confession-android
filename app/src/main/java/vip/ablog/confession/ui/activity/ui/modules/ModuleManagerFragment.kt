package vip.ablog.confession.ui.activity.ui.modules

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.activity.WebModuleDetailActivity
import vip.ablog.confession.ui.adapter.ModuleItemAdapter
import vip.ablog.confession.ui.model.ModuleItemBean
import vip.ablog.confession.utils.*
import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class ModuleManagerFragment : Fragment() {
    lateinit var contentView:View
    lateinit var  rv_fragment_module_manager:RecyclerView
    private var moduleDataList: MutableList<ModuleItemBean> = ArrayList()
    private lateinit var moduleItemAdapter: ModuleItemAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        contentView = inflater.inflate(R.layout.fragment_module_manager, container, false)
        rv_fragment_module_manager = contentView.findViewById(R.id.rv_fragment_module_manager)
        val fab: FloatingActionButton = contentView.findViewById(R.id.fab)
        fab.setOnLongClickListener(object : AdapterView.OnItemLongClickListener,
            View.OnLongClickListener {
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {

                return true
            }

            override fun onLongClick(v: View?): Boolean {
                ToastUtils.showToast(activity,"导入模板",0)
                return true
            }

        })
        fab.setOnClickListener { view ->
//ACTION_GET_CONTENT ACTION_OPEN_DOCUMENT
            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "zip/*"

            startActivityForResult(Intent.createChooser(intent, "选择ZIP格式模板"), 101)
//ACTION_PICK
            /* val intent = Intent()
             intent.action = Intent.ACTION_PICK
             intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
             startActivityForResult(Intent.createChooser(intent, "选择ZIP格式模板"), 101)*/
        }
        initData()

        return contentView
    }


    fun initData() {
   /*     var dir = File(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE)
        var list = dir.list()
        for (i in list) {

            val module = ModuleItemBean()
            module.name = i
            module.cover = Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + i +File.separator+ "cover.jpg"
            module.previewPath =
                "file://" + Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + i +File.separator+ "index.html"
            module.modulePath =
                Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + i +File.separator+ "index.ftl"
            moduleDataList.add(module)
        }*/
        moduleDataList = FileReaderUtil.getLocalStorageModules()
        // 设置新的数据方法
        moduleItemAdapter = ModuleItemAdapter(moduleDataList, activity)
        rv_fragment_module_manager.adapter = moduleItemAdapter
        rv_fragment_module_manager.layoutManager = LinearLayoutManager(activity)
        moduleItemAdapter.setNewInstance(moduleDataList)
        initListener()
    }

    fun initListener() {
        rv_fragment_module_manager?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                var layoutManager: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
                var firstCompletelyVisibleItemPosition = layoutManager?.findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleItemPosition == 0) {
                    //activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                }
                var lastCompletelyVisibleItemPosition = layoutManager?.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager?.getItemCount()!! - 1) {
                   // activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                }
            }
        } )

        moduleItemAdapter?.setOnItemClickListener(object: OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                println("item:"+position + "click!：data：  " + moduleDataList[position].previewPath)
                var intent = Intent()
                activity?.let {
                    intent.setClass(it, WebModuleDetailActivity::class.java)
                    intent.putExtra(Constant.PREVIEW_URL, moduleDataList[position].previewPath)
                }
                startActivity(intent)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //多选从getClipData获取，单选从getData获取
        /*      data?.clipData?.let {
                  for (idx in 0 until it.itemCount) {
                      val item = it.getItemAt(idx)
                      val uri = item.uri
                  }
              }
      */
        val baseDir = Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE
        val uri = data?.data ?: return
        val fileName = getFileRealNameFromUri(activity, uri)

        val copyResult =
            FileUtils.copyFile(activity, baseDir+File.separator+fileName+".zip" , uri)

        if (copyResult){
            val result = ZipUtil.upZipFile(baseDir + File.separator + fileName + ".zip", baseDir+File.separator)
            if (result){
                ToastUtils.showToast(activity,"模板导入成功！",1);
                moduleDataList.clear()
                this.moduleItemAdapter.notifyDataSetChanged()
                moduleDataList = FileReaderUtil.getLocalStorageModules()
                moduleItemAdapter.setNewData(moduleDataList)
            }else{
                ToastUtils.showToast(activity,"解压导入的模板文件失败！",1);
            }
        }else{

            ToastUtils.showToast(activity,"导入文件失败！复制过程出错",1);
        }





    }

    fun getFileRealNameFromUri(context: Context?, fileUri: Uri?): String? {
        if (context == null || fileUri == null) return null
        val documentFile: DocumentFile = DocumentFile.fromSingleUri(context, fileUri) ?: return null
        return documentFile.getName()
    }

}
