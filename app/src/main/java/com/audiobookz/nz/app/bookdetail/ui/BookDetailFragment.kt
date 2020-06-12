package com.audiobookz.nz.app.bookdetail.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.browse.BrowseFragment.ViewPagerAdapter
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBookDetailBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.setTitle
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.intentShareText
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class BookDetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BookDetailViewModel
    private val args: BookDetailFragmentArgs by navArgs()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                intentShareText(activity!!, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                "testShare")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModel(viewModelFactory)
        viewModel.bookId = args.id.toString()
        //   viewModel.fetchReview(1, REVIEW_PAGE_SIZE)

        val binding = FragmentBookDetailBinding.inflate(inflater, container, false)

        context ?: return binding.root

        binding.viewModel = viewModel

        val adapter = ReviewViewPagerAdapter(childFragmentManager)

        subscribeUi(binding, adapter)

        args.bookName?.let { setTitle(it) }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(
        binding: FragmentBookDetailBinding,
        adapter: ReviewViewPagerAdapter
    ) {

        viewModel.bookData.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    bindView(binding, result.data)
                    //add fragment to pagerView
                    adapter.addFragment(SummaryFragment(result.data?.description!!), "Summary")
                    adapter.addFragment(ReviewsFragment(args.id.toString()), "Review")

                    binding.viewPagerReview.adapter = adapter

                    binding.tabBookDetail.setupWithViewPager(binding.viewPagerReview)

                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.addCartResult.observe(viewLifecycleOwner, Observer { result ->
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
        viewModel.addCartResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    Snackbar.make(
                        binding.root,
                        "This Book is Successfully added to your Cart",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                Result.Status.ERROR -> {
                    Snackbar.make(
                        binding.root,
                        "This Book is already added to your Cart",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun bindView(binding: FragmentBookDetailBinding, bookDetail: BookDetail?) {
        bookDetail.apply {
            binding.progressBar.hide()
            binding.book = bookDetail
            binding.rating = bookDetail?.avg_rating?.toFloat()
            binding.authors =
                bookDetail?.BookEngineData?.BookDetail?.authors?.joinToString(separator = ",")
            binding.narrate =
                bookDetail?.BookEngineData?.BookDetail?.narrators?.joinToString(separator = ",")

        }
    }

    internal class ReviewViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(
            fragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        private val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        private val titles: ArrayList<String> = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int = fragments.size


        override fun getPageTitle(i: Int): CharSequence? = titles[i]


        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }

}