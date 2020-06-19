package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.audiobookList.ui.AudiobookListViewModel
import com.audiobookz.nz.app.databinding.FragmentCloudLibraryBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CloudLibraryFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyLibraryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var rootView = FragmentCloudLibraryBinding.inflate(inflater, container, false)
        val adapter = CloudLibraryAdapter(activity!!)
        context ?: return rootView.root
        rootView.CouldRecyclerView.adapter = adapter
        viewModel.getCloudBook(1, CLOUDBOOK_PAGE_SIZE)
        subscribeUi(rootView, adapter)

        return rootView.root
    }

    private fun subscribeUi(binding: FragmentCloudLibraryBinding, adapter: CloudLibraryAdapter) {

        viewModel.cloudBookResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                 //   result.data?.let { adapter.submitList(it["bookList"]) }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}
