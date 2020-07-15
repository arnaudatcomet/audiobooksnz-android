package com.audiobookz.nz.app.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentAudioPlayerBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModelWithActivityLifeCycle
import com.audiobookz.nz.app.ui.setTitle
import com.audiobookz.nz.app.util.THIRTY_MILI_SEC
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.audiobookz.nz.app.data.Result
import com.bumptech.glide.Glide
import io.audioengine.mobile.PlaybackEvent
import kotlinx.android.synthetic.main.fragment_book_detail.view.*
import kotlinx.android.synthetic.main.fragment_rate_and_review.view.*
import kotlinx.android.synthetic.main.rating_dialog_layout.view.*
import kotlinx.android.synthetic.main.rating_dialog_layout.view.reviewCommentTxt
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AudioPlayerFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    lateinit var extraCloudBookId: String
    lateinit var extraContentID: String
    lateinit var extraLicenseId: String
    lateinit var extraImageUrl: String
    lateinit var extraBookTitle: String
    lateinit var extraBookId: String
    var statePlay = true
    var durationLeft = 0L
    private lateinit var fragmentStatus: String
    private var currentPositionPlay: Long? = 0
    private var currentPlayChapter = 0
    private var currentPlayPart = 0
    private var chapterBookTxt: TextView? = null
    private var bookmarkId = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentStatus = "onAttach"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModelWithActivityLifeCycle(viewModelFactory)
        var binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        chapterBookTxt = activity?.findViewById(R.id.titleBook)
        extraContentID = activity?.intent?.getStringExtra("contentId").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        extraCloudBookId = activity?.intent?.getStringExtra("cloudBookId").toString()
        extraBookTitle = activity?.intent?.getStringExtra("titleBook").toString()
        extraImageUrl = activity?.intent?.getStringExtra("urlImage").toString()
        extraBookId = activity?.intent?.getStringExtra("bookId").toString()

        setTitle(extraBookTitle)
        binding.urlImage = extraImageUrl
        binding.speedSelectClick = speedSelectClick()
        binding.chapterSelectClick = chapterSelectClick()
        binding.sleepTimeSelectClick = sleepTimeClick()
        binding.addToBookmarkClick = addBookmarkClick()
        binding.skipPreviousClick = skipPreviousClick()
        binding.skipNextClick = skipNextClick()
        binding.forward30sClick = forward30sClick()
        binding.replay30sClick = replay30sClick()
        binding.playClick = playClick()
        viewModel.getChapters(extraContentID)
        subscribeUi(binding)
        viewModel.getPlayerState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //use seekbar select position player
        var seekbarTime = view.findViewById<SeekBar>(R.id.progressPlayerTimePlay)
        seekbarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressNewPosition: Int = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressNewPosition = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(progressNewPosition.toLong())
            }

        })
    }

    private fun skipPreviousClick(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.previousChapter()
        }
    }

    private fun skipNextClick(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.nextChapter()
        }
    }

    private fun playClick(): View.OnClickListener {
        return View.OnClickListener {
            when (statePlay) {
                true -> {
                    viewModel.pauseAudioBook()
                }
                else -> {
                    viewModel.resumeAudioBook()
                }
            }
        }
    }

    private fun forward30sClick(): View.OnClickListener {
        return View.OnClickListener {
            if (durationLeft > THIRTY_MILI_SEC) {
                viewModel.seekTo(currentPositionPlay!! + THIRTY_MILI_SEC)
            }
        }
    }

    private fun replay30sClick(): View.OnClickListener {
        return View.OnClickListener {
            if (currentPositionPlay!! > THIRTY_MILI_SEC) {
                viewModel.seekTo(currentPositionPlay!! - THIRTY_MILI_SEC)
            }
        }
    }

    private fun speedSelectClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                AudioPlayerFragmentDirections.actionAudioPlayerFragmentToPlayerSpeedFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun chapterSelectClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                AudioPlayerFragmentDirections.actionAudioPlayerFragmentToChapterFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun sleepTimeClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                AudioPlayerFragmentDirections.actionAudioPlayerFragmentToPlayerSleepTimeFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun addBookmarkClick(): View.OnClickListener {
        return View.OnClickListener {

            viewModel.postBookmarks(
                extraCloudBookId.toInt(),
                currentPlayChapter.toString(),
                currentPositionPlay.toString(),
                "Chapter $currentPlayChapter / $currentPositionPlay",
                currentPlayPart.toString(),
                ""
            )

            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.createBookmark))

                .setNegativeButton(resources.getString(R.string.withoutNote)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.addNote)) { dialog, which ->
                    val direction =
                        AudioPlayerFragmentDirections.actionAudioPlayerFragmentToBookmarkNoteFragment(
                            bookmarkId
                        )
                    it.findNavController().navigate(direction)
                }
                .show()
        }
    }

    fun convertAndUpdateTime(position: Long, duration: Long, binding: FragmentAudioPlayerBinding) {

        var totalDuration = duration
        var spentTime = position

        var hourSpent = TimeUnit.MILLISECONDS.toHours(spentTime)
        var minSpent = TimeUnit.MILLISECONDS.toMinutes(spentTime) % TimeUnit.HOURS.toMinutes(1)
        var secSpent = TimeUnit.MILLISECONDS.toSeconds(spentTime) % TimeUnit.MINUTES.toSeconds(1)

        durationLeft = (totalDuration - spentTime)
        var hourLeft = TimeUnit.MILLISECONDS.toHours(durationLeft)
        var minLeft = TimeUnit.MILLISECONDS.toMinutes(durationLeft) % TimeUnit.HOURS.toMinutes(1)
        var secLeft = TimeUnit.MILLISECONDS.toSeconds(durationLeft) % TimeUnit.MINUTES.toSeconds(1)

        binding.progressPlayerTimePlay.progress = position.toInt()
        binding.progressPlayerTimePlay.max = duration.toInt()

        if (durationLeft > 0) {
            if (hourLeft != 0L) {
                binding.timeLeftTxt.text =
                    String.format("%02d:%02d:%02d", hourLeft, minLeft, secLeft)
            } else {
                binding.timeLeftTxt.text = String.format("%02d:%02d", minLeft, secLeft)
            }

            if (hourSpent == 0L) {
                binding.timeSpentTxt.text = String.format("%02d:%02d", minSpent, secSpent)
            } else {
                binding.timeSpentTxt.text =
                    String.format("%02d:%02d:%02d", hourSpent, minSpent, secSpent)
            }
        }

    }

    private fun customReviewDialog() {
        //go to main Activity when read all chapter
        val intent = Intent(activity, MainActivity::class.java)

        val customDialogView =
            LayoutInflater.from(activity).inflate(R.layout.rating_dialog_layout, null)
        val customBuilder = activity?.let { AlertDialog.Builder(it).setView(customDialogView) }
        val customAlertDialog = customBuilder?.show()

        customDialogView.dialogTitle.text = "$extraBookTitle"
        Glide.with(this).load(extraImageUrl).into(customDialogView.dialogImage)

        customDialogView.reviewBtn.setOnClickListener {

            if (!customDialogView.reviewCommentTxt.text.isNullOrBlank()) {
                customAlertDialog?.dismiss()
                var commentText = customDialogView.reviewCommentTxt.text.toString()
                var rateSatisfaction = customDialogView.dRatingSatisfactionBar.rating
                var rateNarrator = customDialogView.dRatingNarrationBar.rating
                var rateAuthor = customDialogView.dRatingStoryBar.rating
                viewModel.postBookReview(
                    extraBookId.toInt(),
                    commentText,
                    rateSatisfaction,
                    rateAuthor,
                    rateNarrator
                )

            } else {
                Toast.makeText(activity, "comment is blank", Toast.LENGTH_SHORT).show();3
                startActivity(intent)
                activity?.finish()
            }

        }

        customDialogView.cancelBtn.setOnClickListener {
            customAlertDialog?.dismiss()
            startActivity(intent)
            activity?.finish()
        }

    }

    private fun subscribeUi(binding: FragmentAudioPlayerBinding) {
        //check come from button option
        if (fragmentStatus == "onAttach") {
            fragmentStatus = "onViewCreated"
            viewModel.listChapterResult.observe(viewLifecycleOwner, Observer { result ->
                viewModel.playAudioBook(
                    result[0].chapter,
                    extraContentID,
                    extraLicenseId,
                    result[0].part
                )

                //sum book duration if first time
                if (viewModel.getBookDuration(extraContentID.toInt()) == 0L) {
                    var totalBookDuration = 0L
                    for (chapter in result) {
                        totalBookDuration += chapter.duration
                    }
                    viewModel.saveBookSize(extraContentID.toInt(), result.size)
                    viewModel.saveBookTotalDuration(extraContentID.toInt(), totalBookDuration)
                }

            })
        }

        viewModel.playBackResult.observe(viewLifecycleOwner, Observer { result ->
            currentPlayPart = result.chapter!!.part
            currentPlayChapter = result.chapter!!.chapter
            chapterBookTxt?.text = "Chapter $currentPlayChapter"
            viewModel.saveCurrentChapter(extraContentID.toInt(), currentPlayChapter!!)

            when (result.code) {
                PlaybackEvent.PLAYBACK_PROGRESS_UPDATE -> {
                    currentPositionPlay = result.position
                    result.position?.let {
                        result.duration?.let { it1 ->
                            convertAndUpdateTime(
                                it,
                                it1,
                                binding
                            )
                        }
                    }

                    viewModel.savePositionPlay(
                        currentPositionPlay!!,
                        extraContentID.toInt(),
                        currentPlayChapter!!
                    )
                }

                //save position
                PlaybackEvent.PLAYBACK_ENDED -> {

                    viewModel.savePositionPlay(
                        currentPositionPlay!!,
                        extraContentID.toInt(),
                        currentPlayChapter!!
                    )
                    viewModel.saveBookReadComplete(extraContentID.toInt())

                    customReviewDialog()

                }

                PlaybackEvent.CHAPTER_PLAYBACK_COMPLETED -> {
                    viewModel.postChapterPosition(
                        extraCloudBookId.toInt(),
                        currentPlayChapter!!,
                        currentPositionPlay!!,
                        result.chapter!!.part
                    )
                    viewModel.savePositionPlay(
                        currentPositionPlay!!,
                        extraContentID.toInt(),
                        currentPlayChapter!!
                    )
                }

                PlaybackEvent.PLAYBACK_PAUSED -> {
                    viewModel.savePositionPlay(
                        currentPositionPlay!!,
                        extraContentID.toInt(),
                        currentPlayChapter!!
                    )
                }

            }
        })

        viewModel.playerStateResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.name) {
                "PLAYING" -> {
                    statePlay = true
                    binding.playButton.setImageResource(R.drawable.pause96)
                }
                "PAUSED" -> {
                    statePlay = false
                    binding.playButton.setImageResource(R.drawable.play_arrow96)
                }
            }
        })

        viewModel.positionPostResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    Toast.makeText(activity, "done", Toast.LENGTH_SHORT).show();3
                }
            }
        })

        viewModel.postBookmarksResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    bookmarkId = result.data!!.id
                }
            }
        })

        viewModel.postBookReviewResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        })
    }
}
