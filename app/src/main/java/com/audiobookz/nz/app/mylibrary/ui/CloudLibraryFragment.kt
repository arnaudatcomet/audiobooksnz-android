package com.audiobookz.nz.app.mylibrary.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentCloudLibraryBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CloudLibraryFragment(private var keyword: String) : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var rootView = FragmentCloudLibraryBinding.inflate(inflater, container, false)
        context ?: return rootView.root

        rootView.CloudRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && viewModel.isLatest == false) {
                    viewModel.pageCount?.plus(1)
                        ?.let { viewModel.getCloudBook(it, CLOUDBOOK_PAGE_SIZE, "") }
                    viewModel.pageCount = viewModel.pageCount?.plus(1)
                }
            }
        })

        if (keyword != "") {
            viewModel.getCloudBook(1, 50, keyword)
        } else {
            viewModel.getCloudBook(1, CLOUDBOOK_PAGE_SIZE, "")
        }

        subscribeUi(rootView)
        return rootView.root
    }

    private fun subscribeUi(binding: FragmentCloudLibraryBinding) {

        viewModel.cloudBookResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    val adapter = activity?.let {
                        result.data?.let { it1 ->
                            CloudLibraryAdapter(
                                it,
                                it1,
                                viewModel
                            )
                        }
                    }

                    //add delay to fix item render before data available
                    Handler().postDelayed({
                        binding.CloudRecyclerView.adapter = adapter
                    }, 500)

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
