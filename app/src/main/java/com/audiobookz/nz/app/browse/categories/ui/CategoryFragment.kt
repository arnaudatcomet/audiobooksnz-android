package com.audiobookz.nz.app.browse.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentCategoriesBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class CategoryFragment: Fragment(), Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = CategoryAdapter()
        binding.categoryRecyclerView.addItemDecoration(
            VerticalItemDecoration(resources.getDimension(R.dimen.margin_normal).toInt(), true) )


        binding.categoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) &&viewModel.isLatest==false) {
                    viewModel.page?.plus( 1)?.let { viewModel.fetchCategory(it,CATEGORY_PAGE_SIZE) }
                    viewModel.page=viewModel.page?.plus(1)
                }
            }
        })


        binding.categoryRecyclerView.adapter = adapter
        viewModel.fetchCategory(viewModel.page!!,CATEGORY_PAGE_SIZE)
        subscribeUi(binding, adapter)
        return binding.root
    }
    private fun subscribeUi(binding: FragmentCategoriesBinding, adapter: CategoryAdapter) {
        viewModel.categoryResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
    companion object {
        fun newInstance(): CategoryFragment =
            CategoryFragment()
    }

}