package com.audiobookz.nz.app.mylibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.binding.TabLayoutAdapter
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.player.ui.PlayerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class MyLibraryFragment : Fragment() , Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyLibraryViewModel
    lateinit var adapter : TabLayoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView = inflater.inflate(R.layout.fragment_mylibrary, container, false)
        setHasOptionsMenu(true)
        viewModel = injectViewModel(viewModelFactory)
        return rootView
    }

    //filter cloudBook library
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var searchCloud = view.findViewById<SearchView>(R.id.filterBookView)
        var libraryViewPager = view.findViewById<ViewPager>(R.id.tab_view_pager_library)
        var libraryTabLayout = view.findViewById<TabLayout>(R.id.tab_my_library)
        var playBtn = view.findViewById<FloatingActionButton>(R.id.imgPlayLibrary)

        adapter = TabLayoutAdapter(childFragmentManager)
        adapter.addFragment(CloudLibraryFragment(""), "Cloud")
        adapter.addFragment(DeviceLibraryFragment(), "Device")
        libraryViewPager.adapter = adapter
        libraryTabLayout.setupWithViewPager(libraryViewPager)

        playBtn.setOnClickListener {
            var bookDetail = viewModel?.getMultiValueCurretBook
            if (bookDetail != null) {
                val intent = Intent(activity, PlayerActivity::class.java).apply {
                    putExtra("contentId", bookDetail[0])
                    putExtra("licenseIDBook", bookDetail[1])
                    putExtra("cloudBookId", bookDetail[2])
                    putExtra("titleBook", bookDetail[3])
                    putExtra("urlImage", bookDetail[4])
                    putExtra("bookId", bookDetail[5])
                    putExtra("authorBook", bookDetail[6])
                    putExtra("narratorBook", bookDetail[7])
                }
                startActivity(intent)
            }
        }

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
