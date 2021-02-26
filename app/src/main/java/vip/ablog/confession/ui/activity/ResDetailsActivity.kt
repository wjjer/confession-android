package vip.ablog.confession.ui.activity

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import vip.ablog.confession.R
import vip.ablog.confession.global.Constant
import vip.ablog.confession.ui.activity.ui.main.CommentFragment
import vip.ablog.confession.ui.activity.ui.main.ResDetailsFragment
import vip.ablog.confession.ui.activity.ui.main.SectionsPagerAdapter

class ResDetailsActivity : AppCompatActivity() {

    private lateinit var tv_res_title: TextView
    var resDetialFragment: Fragment = ResDetailsFragment()
    var commentFragment: Fragment = CommentFragment()
    var fragmentList: MutableList<Fragment> = mutableListOf(resDetialFragment, commentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_details)

        initView()


    }

    private fun initView() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, fragmentList)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        var title = this.intent.getStringExtra(Constant.RES_TITLE)
        tv_res_title = findViewById(R.id.tv_res_title)
        tv_res_title.setText(title)
        viewPager.offscreenPageLimit = 0
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}