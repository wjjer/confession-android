package vip.ablog.confession.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import vip.ablog.confession.R
import vip.ablog.confession.ui.activity.ui.modules.ModuleManagerFragment
import vip.ablog.confession.ui.activity.ui.modules.ModuleMarketFragment
import vip.ablog.confession.ui.adapter.ModulePagerAdapter

class CategoryFragment : Fragment() {

    private lateinit var btnPreview: Button
    var marketFragment: Fragment = ModuleMarketFragment()
    var managerFragment: Fragment = ModuleManagerFragment()
    var fragmentList: MutableList<Fragment> = mutableListOf(managerFragment, marketFragment)
    lateinit var pagerAdapter:ModulePagerAdapter;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_category, container, false)
        initView(root)
        return root
    }

    fun initView(root: View) {
        pagerAdapter =
            activity?.let { ModulePagerAdapter(it, childFragmentManager, fragmentList) }!!
        val viewPager: ViewPager = root.findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = root.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }


}