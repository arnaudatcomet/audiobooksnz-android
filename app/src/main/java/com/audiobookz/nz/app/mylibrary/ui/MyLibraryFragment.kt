package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.binding.TabLayoutAdapter
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_mylibrary.view.*
import javax.inject.Inject

class MyLibraryFragment : Fragment() {
    lateinit var adapter : TabLayoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_mylibrary, container, false)
        setHasOptionsMenu(true)

        return rootView
    }

    //filter cloudBook library
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var searchCloud = view.findViewById<SearchView>(R.id.filterBookView)
        var libraryViewPager = view.findViewById<ViewPager>(R.id.tab_view_pager_library)
        var libraryTabLayout = view.findViewById<TabLayout>(R.id.tab_my_library)

        adapter = TabLayoutAdapter(childFragmentManager)
        adapter.addFragment(CloudLibraryFragment(""), "Cloud")
        adapter.addFragment(DeviceLibraryFragment(), "Device")
        libraryViewPager.adapter = adapter
        libraryTabLayout.setupWithViewPager(libraryViewPager)

        searchCloud.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //do noting
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.deleteFragment()
                adapter.addFragment(CloudLibraryFragment(query), "Cloud")
                adapter.addFragment(DeviceLibraryFragment(), "Device")
                (libraryViewPager.adapter as TabLayoutAdapter).notifyDataSetChanged()

                return false
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_search, menu)
        val searchView: SearchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //do noting
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                val navController = Navigation.findNavController(view!!)
                navController.navigate(
                    MyLibraryFragmentDirections.actionMylibraryToAudiobookListFragment(
                        id = 0, keyword = query, titleList = "\"$query\""
                    )
                )

                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

}
