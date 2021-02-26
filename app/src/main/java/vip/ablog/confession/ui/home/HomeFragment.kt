package vip.ablog.confession.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import vip.ablog.confession.R
import vip.ablog.confession.ui.activity.ui.main.CommentFragment
import vip.ablog.confession.ui.activity.ui.main.ResDetailsFragment
import vip.ablog.confession.ui.activity.ui.main.SectionsPagerAdapter

class HomeFragment : Fragment() {
    private lateinit var tv_res_title: TextView
    lateinit var fragmentList: MutableList<Fragment>
    lateinit var contentView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var resDetialFragment: Fragment = ResDetailsFragment()
        var commentFragment: Fragment = CommentFragment()
        fragmentList = mutableListOf(resDetialFragment, commentFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.activity_res_details, container, false)
        initView(contentView)
        return contentView
    }
    private fun initView(view:View) {
        val sectionsPagerAdapter =
            activity?.let { childFragmentManager?.let { it1 ->
                SectionsPagerAdapter(it,
                    it1, fragmentList)
            } }
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        tv_res_title = view.findViewById(R.id.tv_res_title)
        tv_res_title.setText("首页")



    }


}


