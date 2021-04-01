package com.audiobookz.nz.app.browse

import android.os.Bundle
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.binding.TabLayoutAdapter
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.browse.featured.ui.FeaturedFragment
import com.audiobookz.nz.app.di.Injectable
import kotlinx.android.synthetic.main.fragment_browse.view.*


class BrowseFragment : Fragment(), Injectable {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_browse, container, false)

        val adapter = TabLayoutAdapter(childFragmentManager)
        adapter.addFragment(FeaturedFragment(), "Featured")
        adapter.addFragment(CategoryFragment(), "Category")
        rootView.tab_view_pager.adapter = adapter
        rootView.tab_browse.setupWithViewPager(rootView.tab_view_pager)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_search, menu)
        val searchView: SearchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchTextView =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.app_bar_search_cursor)
        } catch (e: Exception) {
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //do noting
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                val navController = Navigation.findNavController(view!!)
                navController.navigate(
                    BrowseFragmentDirections.actionBrowseFragmentToAudiobookListFragment(
                        id = 0, keyword = query, titleList = "\"$query\""
                    )
                )

                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance(): BrowseFragment =
            BrowseFragment()
    }
}
