package com.audiobookz.nz.app.bookdetail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.binding.bindImageFromUrl
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.browse.BrowseFragment
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBookDetailBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.REVIEW_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class BookDetailFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BookDetailViewModel
    private val args: BookDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModel(viewModelFactory)
        viewModel.bookId = args.id.toString()
        viewModel.fetchReview(1,REVIEW_PAGE_SIZE)

        val binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        val adapter = BrowseFragment.ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SummaryFragment(), "Summary")
        adapter.addFragment(ReviewsFragment(), "Reviews")
        binding.tabBookDetailPager.adapter = adapter
        binding.tabBookDetail.setupWithViewPager(binding.tabBookDetailPager)

        context ?: return binding.root

        binding.viewModel = viewModel

        subscribeUi(binding)
        return binding.root
    }
    private fun subscribeUi(binding: FragmentBookDetailBinding) {
        viewModel.bookData.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    bindView(binding,result.data)
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.reviewResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
        viewModel.addCartResult.observe(viewLifecycleOwner, Observer { result->
            when(result.status)
            {
                Result.Status.SUCCESS -> {
                    Snackbar.make(binding.root, "added", Snackbar.LENGTH_LONG).show()
                }
                Result.Status.ERROR -> {
                    Snackbar.make(binding.root, "already add", Snackbar.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun bindView(binding: FragmentBookDetailBinding, bookDetail: BookDetail?) {
        bookDetail.apply {
            binding.progressBar.hide()
            binding.book = bookDetail
            binding.rating = bookDetail?.avg_rating?.toFloat()
            binding.authors = bookDetail?.BookEngineData?.BookDetail?.authors?.joinToString(separator = ",")
            binding.narrate = bookDetail?.BookEngineData?.BookDetail?.narrators?.joinToString(separator = ",")

        }
    }

}