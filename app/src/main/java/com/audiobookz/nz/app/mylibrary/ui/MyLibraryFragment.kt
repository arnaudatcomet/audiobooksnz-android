package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.binding.TabLayoutAdapter
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import kotlinx.android.synthetic.main.fragment_mylibrary.view.*

class MyLibraryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_mylibrary, container, false)

        val adapter = TabLayoutAdapter(childFragmentManager)
        adapter.addFragment(CloudLibraryFragment(), "Cloud")
        adapter.addFragment(DeviceLibraryFragment(), "Device")
        rootView.tab_view_pager_library.adapter = adapter
        rootView.tab_my_library.setupWithViewPager(rootView.tab_view_pager_library)
        setHasOptionsMenu(true)

        return rootView
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
