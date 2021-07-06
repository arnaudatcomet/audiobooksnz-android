package com.audiobookz.nz.app.bookdetail.ui

import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class BookDetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BookDetailViewModel
    private val args: BookDetailFragmentArgs by navArgs()
    private lateinit var mediaPlayer: MediaPlayer

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
        viewModel.showItemAnalytic()

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

    private fun bindView(binding: FragmentBookDetailBinding, bookDetail: BookDetail?) {
        bookDetail.apply {
            var listBookAuthor = bookDetail?.BookEngineData?.BookDetail?.authors
            var listBookNarrator = bookDetail?.BookEngineData?.BookDetail?.narrators
            var bookPubliser = bookDetail?.BookEngineData?.BookDetail?.publisher

            binding.progressBar.hide()
            //underline text
            binding.authorName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.narratorName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.publisherName.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            mediaPlayer.setDataSource(bookDetail?.BookEngineData?.BookDetail?.sampleUrl)
            mediaPlayer.prepare()

            binding.playSimpleClick = playSample(binding)
            binding.wishListClick = addWishList(binding)
            binding.authorsClick = authorSearch(listBookAuthor!!.toTypedArray())
            binding.narratorClick = narratorSearch(listBookNarrator!!.toTypedArray())
            binding.publisherClick = publisherSearch(bookPubliser)
            binding.isBought = bookDetail?.is_bought
            binding.inWishlist = bookDetail?.in_wishlist
            binding.book = bookDetail
            binding.rating = bookDetail?.avg_rating?.toFloat()
            binding.authors = listBookAuthor?.joinToString(separator = ",")
            binding.narrate = listBookNarrator?.joinToString(separator = ",")
            if (bookDetail?.BookEngineData?.BookDetail?.series?.size != 0) {
                binding.series = bookDetail?.BookEngineData?.BookDetail?.series?.get(0)
            } else {
                binding.series = ""
            }

        }
    }

    private fun playSample(binding: FragmentBookDetailBinding): View.OnClickListener {
        return View.OnClickListener {

            if (mediaPlayer.isPlaying) {
                binding.playSampleBtn.text = "Play Sample"
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                binding.playSampleBtn.text = "Pause Sample"
            }

        }
    }

    private fun addWishList(binding: FragmentBookDetailBinding): View.OnClickListener {
        return View.OnClickListener {
            binding.addToWishListBtn.visibility = View.GONE
            viewModel.addWishList(args.id)
        }
    }

    private fun authorSearch(bookAuthor: Array<String>): View.OnClickListener {
        return View.OnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.searchByAuthor))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(bookAuthor) { dialog, which ->
                    var author = bookAuthor[which]
                    val navController = Navigation.findNavController(view!!)
                    navController.navigate(
                        BookDetailFragmentDirections.actionBookDetailFragmentToAudiobookListFragment(
                            id = 0, keyword = author, titleList = "Author: \"$author\""
                        )
                    )
                }
                .show()

        }
    }

    private fun narratorSearch(bookNarrator: Array<String>): View.OnClickListener {
        return View.OnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.searchByNarrator))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(bookNarrator) { dialog, which ->
                    var narrator = bookNarrator[which]
                    val navController = Navigation.findNavController(view!!)
                    navController.navigate(
                        BookDetailFragmentDirections.actionBookDetailFragmentToAudiobookListFragment(
                            id = 0, keyword = narrator, titleList = "Narrator: \"$narrator\""
                        )
                    )
                }
                .show()
        }
    }

    private fun publisherSearch(bookPubliser: String?): View.OnClickListener {
        return View.OnClickListener {
            val navController = Navigation.findNavController(view!!)
            navController.navigate(
                BookDetailFragmentDirections.actionBookDetailFragmentToAudiobookListFragment(
                    id = 0, keyword = bookPubliser, titleList = "\"$bookPubliser\""
                )
            )
        }
    }

    private fun subscribeUi(binding: FragmentBookDetailBinding, adapter: TabLayoutAdapter) {

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
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
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

        viewModel.resultAddWishList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.addToWishListBtn.visibility = View.GONE
                }
            }
        })
    }

}