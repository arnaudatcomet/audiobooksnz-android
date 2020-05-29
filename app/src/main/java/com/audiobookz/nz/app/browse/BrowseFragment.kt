package com.audiobookz.nz.app.browse

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.browse.featured.ui.FeaturedFragment
import com.audiobookz.nz.app.di.Injectable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_browse.view.*
import javax.inject.Inject

class BrowseFragment : Fragment(), Injectable {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_browse, container, false)
        val rootView = inflater.inflate(R.layout.fragment_browse, container, false)

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FeaturedFragment(), "Featured")
        adapter.addFragment(CategoryFragment(), "Category")
        rootView.tab_view_pager.adapter = adapter
        rootView.tab_browse.setupWithViewPager(rootView.tab_view_pager)

        return rootView
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(
            fragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
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
