package com.audiobookz.nz.app.bookdetail.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentReviewsBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.util.REVIEW_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ReviewsFragment(val id: String) : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BookDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = FragmentReviewsBinding.inflate(inflater, container, false)
        context ?: return binding.root



        var reviewAdapter = ReviewAdapter()

        binding.recycleViewReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && viewModel.isLatest == false) {
                    viewModel.page?.plus(1)?.let { viewModel.fetchReview(it, REVIEW_PAGE_SIZE) }
                    viewModel.page = viewModel.page?.plus(1)

                }
            }
        })

        binding.recycleViewReview.adapter = reviewAdapter
        viewModel.bookId = id
        viewModel.fetchReview(1, REVIEW_PAGE_SIZE)
        subscribeUi(binding, reviewAdapter)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentReviewsBinding, adapter: ReviewAdapter) {
        viewModel.reviewResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    adapter.submitList(result.data)
                    binding.recycleViewReview.adapter = adapter
                }
                Result.Status.LOADING -> { Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show() }
                Result.Status.ERROR -> {Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show() }
            }
        })

    }

}
