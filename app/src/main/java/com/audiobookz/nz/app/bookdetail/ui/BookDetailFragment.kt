package com.audiobookz.nz.app.bookdetail.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.binding.TabLayoutAdapter
import com.audiobookz.nz.app.bookdetail.data.BookDetail
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
    private lateinit var mediaPlayer:MediaPlayer

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)

        //hide menu player when come from playerActivity
        var menuItemPlayer: MenuItem? = null
        menuItemPlayer = menu.findItem(R.id.action_option_player)

        if (menuItemPlayer != null) {
            menuItemPlayer.isVisible = false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                intentShareText(
                    activity!!, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                    "testShare"
                )
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
        mediaPlayer = MediaPlayer()
        val binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.viewModel = viewModel

        val adapter = TabLayoutAdapter(childFragmentManager)

        subscribeUi(binding, adapter)

        args.bookName?.let { setTitle(it) }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(
        binding: FragmentBookDetailBinding,
        adapter: TabLayoutAdapter
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
                    Snackbar.make(
                        binding.root,
                        "This Book is Successfully added to your Cart",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
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

            mediaPlayer.setDataSource(bookDetail?.BookEngineData?.BookDetail?.sampleUrl)
            mediaPlayer.prepare()

            binding.playSimpleClick = playSample(binding)
            binding.wishListClick = addWishList()
            binding.bought = args.isBuy
            binding.book = bookDetail
            binding.rating = bookDetail?.avg_rating?.toFloat()
            binding.authors =
                bookDetail?.BookEngineData?.BookDetail?.authors?.joinToString(separator = ",")
            binding.narrate =
                bookDetail?.BookEngineData?.BookDetail?.narrators?.joinToString(separator = ",")
        }
    }

    private fun playSample(binding: FragmentBookDetailBinding): View.OnClickListener {
        return View.OnClickListener {

            if (mediaPlayer.isPlaying){
                binding.playSampleBtn.text = "Play Sample"
                mediaPlayer.pause()
            }else{
                mediaPlayer.start()
                binding.playSampleBtn.text = "Pause Sample"
            }

        }
    }

    private fun addWishList(): View.OnClickListener {
        return View.OnClickListener {}
    }

}