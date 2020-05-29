package com.audiobookz.nz.app.browse.categories.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.profile.ui.ProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class BrowseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        var tabsFrag = findViewById<TabLayout>(R.id.tab_browse)
        var viewPager = findViewById<ViewPager>(R.id.tab_view_pager)

        viewPagerAdapter.addFragment(ProfileFragment(),"Featured")
        viewPagerAdapter.addFragment(CategoryFragment(),"Category")

        viewPager.adapter = viewPagerAdapter
        tabsFrag.setupWithViewPager(viewPager)


    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        private val titles: ArrayList<String> = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(i: Int): CharSequence? {
            return titles[i]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

    }
}
