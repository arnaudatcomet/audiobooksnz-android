package com.audiobookz.nz.app.binding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabLayoutAdapter (fragmentManager: FragmentManager) :
    FragmentPagerAdapter(
        fragmentManager,
        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    private val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
    private val titles: ArrayList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(i: Int): CharSequence? = titles[i]

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

}