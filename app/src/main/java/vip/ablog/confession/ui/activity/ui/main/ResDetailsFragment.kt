package vip.ablog.confession.ui.activity.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.activity.WebModuleDetailActivity
import vip.ablog.confession.ui.activity.ui.home.GenerateActivity
import vip.ablog.confession.ui.adapter.HomeGaoBaiAdapter
import vip.ablog.confession.ui.model.ModuleItemBean
import vip.ablog.confession.utils.*

/**
 * A placeholder fragment containing a simple view.
 */
class ResDetailsFragment : Fragment() {

    lateinit var rv_fragment_gaobai: RecyclerView
    private var gaoBaiDataList: MutableList<ModuleItemBean> = ArrayList()
    private lateinit var homeGaoBaiAdapter: HomeGaoBaiAdapter;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_res_details, container, false)
        rv_fragment_gaobai = root.findViewById(R.id.rv_home_gaobai)
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
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
                ToastUtils.showToast(activity, "新建告白", 0)
                return true
            }

        })
        fab.setOnClickListener { view ->
            var intent = Intent()
            activity?.let {
                intent.setClass(it, GenerateActivity::class.java)
                //intent.putExtra(Constant.PREVIEW_URL, moduleDataList[position].previewPath)
            }
            startActivityForResult(intent, 100)
        }
        initData()
        return root
    }

    private fun initData() {
        gaoBaiDataList = FileReaderUtil.getLocalStorageGaoBai()
        // 设置新的数据方法
        homeGaoBaiAdapter = HomeGaoBaiAdapter(gaoBaiDataList, activity)
        rv_fragment_gaobai.adapter = homeGaoBaiAdapter
        rv_fragment_gaobai.layoutManager = LinearLayoutManager(activity)
        homeGaoBaiAdapter.setNewInstance(gaoBaiDataList)
        homeGaoBaiAdapter.setEmptyView(R.layout.empty_gaobai_view)
        initListener()
    }

    private fun initListener() {
        rv_fragment_gaobai?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {}
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                var layoutManager: LinearLayoutManager? =
                    recyclerView.layoutManager as LinearLayoutManager?
                var firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleItemPosition == 0) {
                   // activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                }
                var lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager?.getItemCount()!! - 1) {
                    //activity?.applicationContext?.let { VibratorUtil.initViarbtor(it) }
                }
            }
        })

        homeGaoBaiAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                println("item:" + position + "click!：data：  " + gaoBaiDataList[position].previewPath)


                /*   object : Thread() {
                       override fun run() {
                           //上传该图片到服务器
                           try {

                               val file = File(path + gaoBaiDataList[position].name + ".zip")
                               val uploadUrl = "http://192.168.31.102:8080/upload"
                               val result: String = FileUploadUtil.uploadFile(file, uploadUrl)
                               val json = JSONObject(result)
                               val json1 = json.getJSONObject("result")
                               println("上传：" + json)
                           } catch (e: Exception) {
                               e.printStackTrace()
                           }
                       }
                   }.start()*/


                var intent = Intent()
                activity?.let {
                    intent.setClass(it, WebModuleDetailActivity::class.java)
                    intent.putExtra(Constant.PREVIEW_URL, gaoBaiDataList[position].previewPath)
                }
                startActivity(intent)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            gaoBaiDataList.clear()
            this.homeGaoBaiAdapter.notifyDataSetChanged()
            gaoBaiDataList = FileReaderUtil.getLocalStorageGaoBai()
            homeGaoBaiAdapter.setNewData(gaoBaiDataList)
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /*    @JvmStatic
            fun newInstance(sectionNumber: Int): Fragment {
                val currentFragment: Fragment
                if (sectionNumber == 1) {
                    currentFragment =  ResDetailsFragment()
                }else{
                    currentFragment = CommentFragment()
                }
                return currentFragment.apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                    }
                }
            }*/
    }
}
