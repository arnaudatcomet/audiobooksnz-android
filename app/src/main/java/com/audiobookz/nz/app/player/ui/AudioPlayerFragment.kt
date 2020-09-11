package com.audiobookz.nz.app.player.ui

import android.app.NotificationManager
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.mediarouter.media.MediaRouteSelector
import androidx.navigation.findNavController
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAudioPlayerBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModelWithActivityLifeCycle
import com.audiobookz.nz.app.ui.setTitle
import com.audiobookz.nz.app.util.THIRTY_MILI_SEC
import com.bumptech.glide.Glide
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.audioengine.mobile.PlaybackEvent
import kotlinx.android.synthetic.main.rating_dialog_layout.view.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AudioPlayerFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    private var castBookSession: CastSession? = null
    private val castContext by lazy { CastContext.getSharedInstance(activity!!.applicationContext) }
    private val nameSpace by lazy { activity!!.applicationContext.getString(R.string.namespace) }
    private var mediaSelector: MediaRouteSelector? = null
    private var isCarCheck = true
    lateinit var mainHandler: Handler

    // var seekbarTime = view.findViewById<SeekBar>(R.id.progressPlayerTimePlay)
    var remoteMediaClient: RemoteMediaClient? = null
    private var currentPositonPlay: Long? = null
    private var currentPart: Int? = null
    private var amanager: AudioManager? = null
    private var currentPlayChapter: Int? = 0
    lateinit var extraBookId: String
    lateinit var extraContentID: String
    lateinit var extraLicenseId: String
    lateinit var sessionId: String
    lateinit var engineAccount: String
    lateinit var extraCloudBookId: String
    lateinit var extraImageUrl: String
    lateinit var extraBookTitle: String
    lateinit var extraBookAuthor: String
    lateinit var notificationManager: NotificationManager
    lateinit var extraBookNarrator: String
    private var mediaSession: MediaSessionCompat? = null
    var statePlay = true
    var durationLeft = 0L
    private lateinit var fragmentStatus: String
    private var currentPositionPlay: Long? = 0
    private var currentPlayPart = 0
    private var chapterBookTxt: TextView? = null
    private var bookmarkId = 0
    lateinit var binding : FragmentAudioPlayerBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentStatus = "onAttach"
    }

    private val sessionManagerListener = object : SessionManagerListener<CastSession> {
        override fun onSessionStarted(p0: CastSession?, p1: String?) {
            onApplicationConnected(p0)
            remoteMediaClient = p0?.remoteMediaClient
        }

        override fun onSessionResumeFailed(p0: CastSession?, p1: Int) {
        }

        override fun onSessionSuspended(p0: CastSession?, p1: Int) {
            amanager?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_TOGGLE_MUTE,
                0
            );
        }

        override fun onSessionEnded(p0: CastSession?, p1: Int) {
            amanager?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_TOGGLE_MUTE,
                0
            );
        }

        override fun onSessionResumed(p0: CastSession?, p1: Boolean) {
            onApplicationConnected(p0)
            remoteMediaClient = p0?.remoteMediaClient
        }

        override fun onSessionStarting(p0: CastSession?) {
        }

        override fun onSessionResuming(p0: CastSession?, p1: String?) {
        }

        override fun onSessionEnding(p0: CastSession?) {

        }

        override fun onSessionStartFailed(p0: CastSession?, p1: Int) {
        }
    }

    override fun onResume() {
        super.onResume()

        if (activity?.let { isCar(it) } == false) {
            castContext.sessionManager.addSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
            if (castBookSession == null) {

                castBookSession = castContext.sessionManager.currentCastSession
            }
        }
    }

    override fun onPause() {
        Log.i("MainActivity", "onPause")
        if (activity?.let { isCar(it) } == false) {
            castContext.sessionManager.removeSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
        }
        super.onPause()
    }

    fun onApplicationConnected(castSession: CastSession?) {
        amanager?.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
        var seekbarTime = view?.findViewById<SeekBar>(R.id.progressPlayerTimePlay)
        seekbarTime?.progress?.let { loadRemoteMedia(it, true, castSession) }
        return
    }

    private fun loadRemoteMedia(progress: Int, autoPlay: Boolean, castSession: CastSession?) {
        val remoteMediaClient: RemoteMediaClient = castSession?.remoteMediaClient ?: return
        castBookSession = castSession
        remoteMediaClient.load(buildMediaInfo(), buildMediaLoadOption(progress, autoPlay))
    }

    private fun buildMediaLoadOption(progress: Int, autoPlay: Boolean): MediaLoadOptions {
        return MediaLoadOptions.Builder().setAutoplay(autoPlay).setPlayPosition(progress.toLong())
            .build()
    }

    private fun buildMediaInfo(): MediaInfo {
        val mediaMetadata =
            com.google.android.gms.cast.MediaMetadata(com.google.android.gms.cast.MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        mediaMetadata.putString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE, extraBookTitle)
        mediaMetadata.putString(
            com.google.android.gms.cast.MediaMetadata.KEY_ARTIST,
            extraBookAuthor
        )
        val audiobookData = JSONObject()
        audiobookData.put("session_key", sessionId)
        audiobookData.put("account_id", engineAccount)
        audiobookData.put("license_id", extraLicenseId)
        audiobookData.put("part", currentPart)
        audiobookData.put("chapter", currentPlayChapter)
        audiobookData.put("offset", currentPositonPlay)
        return MediaInfo.Builder(extraContentID)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("audio/mpeg")
            .setMetadata(mediaMetadata)
            .setCustomData(audiobookData)
            .setStreamDuration(0)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModelWithActivityLifeCycle(viewModelFactory)
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mediaSession = context?.let { MediaSessionCompat(it, "MusicService") }
        mediaSession!!.setCallback(object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
                handleMediaButton(mediaButtonEvent)
                return super.onMediaButtonEvent(mediaButtonEvent)
            }
        })
        mediaSession?.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession?.isActive = true

        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        chapterBookTxt = activity?.findViewById(R.id.titleBook)
        extraContentID = activity?.intent?.getStringExtra("contentId").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        amanager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        extraCloudBookId = activity?.intent?.getStringExtra("cloudBookId").toString()
        extraBookTitle = activity?.intent?.getStringExtra("titleBook").toString()
        extraImageUrl = activity?.intent?.getStringExtra("urlImage").toString()
        extraBookId = activity?.intent?.getStringExtra("bookId").toString()
        extraBookAuthor = activity?.intent?.getStringExtra("authorBook").toString()
        extraBookNarrator = activity?.intent?.getStringExtra("narratorBook").toString()

        //save detail for call player from floating button play
        var sharePrefBookDetail: ArrayList<String> = arrayListOf(
            extraContentID,
            extraLicenseId,
            extraCloudBookId,
            extraBookTitle,
            extraImageUrl,
            extraBookId,
            extraBookAuthor,
            extraBookNarrator
        )
        viewModel.saveMultiValueCurrentBook(sharePrefBookDetail)

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

        viewModel.getSyncPlayBackPosition(extraCloudBookId.toInt())

        //timer thread
        mainHandler = Handler(Looper.getMainLooper())

        //check first chapter part for play each book
        viewModel.getChapters(extraContentID)
        subscribeUi(binding)
        viewModel.getPlayerState()

        if (activity?.let { isCar(it) } == false) {
            castContext.sessionManager.addSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
            isCarCheck = false
            if (castBookSession == null) {
                castBookSession = castContext.sessionManager.currentCastSession
            }
        }

        binding.isCar = isCarCheck

        return binding.root
    }

    private fun handleMediaButton(mediaButtonEvent: Intent) {
        if (mediaButtonEvent.action == Intent.ACTION_MEDIA_BUTTON) {
            val event = mediaButtonEvent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
            if (event.action == KeyEvent.ACTION_UP) {
                Log.d("TAG", "message" + event.keyCode)
                when (event.keyCode) {
                    KeyEvent.KEYCODE_MEDIA_PLAY, KeyEvent.KEYCODE_MEDIA_PAUSE -> if (statePlay) viewModel.pauseAudioBook() else viewModel.resumeAudioBook()
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> viewModel.previousChapter()
                    KeyEvent.KEYCODE_MEDIA_NEXT -> viewModel.nextChapter()
                }
            }
        }
    }

    private fun updateMetadata(mediaData: PlaybackEvent) {
        mediaSession!!.isActive = true
        val metadataBuilder = MediaMetadataCompat.Builder().apply {
            putString(
                android.media.MediaMetadata.METADATA_KEY_DISPLAY_TITLE,
                mediaData.chapter!!.friendlyName()
            )
            putString(
                android.media.MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
                mediaData.shortMessage
            )
            putString(android.media.MediaMetadata.METADATA_KEY_TITLE, mediaData.content!!.title)
            putString(
                android.media.MediaMetadata.METADATA_KEY_ARTIST,
                mediaData.content!!.narrators.toString()
            )
        }
        mediaSession!!.setMetadata(metadataBuilder.build())
        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY).apply {
                setState(PlaybackStateCompat.STATE_PLAYING, mediaData.duration!!, 1.0f)
            }
        mediaSession!!.setPlaybackState(stateBuilder.build())
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
                remoteMediaClient?.seek(progressNewPosition.toLong())
            }

        })
    }

    private fun skipPreviousClick(): View.OnClickListener {
        return View.OnClickListener {
            var previousChapterChromecastCode: Long = 0;
            viewModel.previousChapter()

            remoteMediaClient?.seek(previousChapterChromecastCode)
        }
    }

    private fun skipNextClick(): View.OnClickListener {
        return View.OnClickListener {
            var nextChapterChromeCastCode: Long = 1;
            viewModel.nextChapter()
            remoteMediaClient?.seek(nextChapterChromeCastCode)
        }
    }

    private fun playClick(): View.OnClickListener {
        return View.OnClickListener {
            when (statePlay) {
                true -> {

                    mainHandler.post(timerPlayerGetSynTask)
                    viewModel.pauseAudioBook()
                    remoteMediaClient?.pause()
                }
                else -> {
                    mainHandler.removeCallbacks(timerPlayerGetSynTask)
                    viewModel.playAudioBook(
                        extraCloudBookId.toInt(),
                        0,
                        extraContentID,
                        extraLicenseId,
                        0
                    )

                    viewModel.resumeAudioBook()
                    remoteMediaClient?.play()
                }
            }
        }
    }

    private fun forward30sClick(): View.OnClickListener {
        return View.OnClickListener {
            if (durationLeft > THIRTY_MILI_SEC) {
                viewModel.seekTo(currentPositionPlay!! + THIRTY_MILI_SEC)
                remoteMediaClient?.seek(currentPositionPlay!! + THIRTY_MILI_SEC)
            }
        }
    }

    private fun replay30sClick(): View.OnClickListener {
        return View.OnClickListener {
            if (currentPositionPlay!! > THIRTY_MILI_SEC) {
                viewModel.seekTo(currentPositionPlay!! - THIRTY_MILI_SEC)
                remoteMediaClient?.seek(currentPositionPlay!! - THIRTY_MILI_SEC)
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

    private fun convertAndUpdateTime(
        position: Long,
        duration: Long,
        binding: FragmentAudioPlayerBinding
    ) {

        var totalDuration = duration
        var spentTime = position

        var hourSpent = TimeUnit.MILLISECONDS.toHours(spentTime)
        var minSpent = TimeUnit.MILLISECONDS.toMinutes(spentTime) % TimeUnit.HOURS.toMinutes(1)
        var secSpent =
            TimeUnit.MILLISECONDS.toSeconds(spentTime) % TimeUnit.MINUTES.toSeconds(1)

        durationLeft = (totalDuration - spentTime)
        var hourLeft = TimeUnit.MILLISECONDS.toHours(durationLeft)
        var minLeft =
            TimeUnit.MILLISECONDS.toMinutes(durationLeft) % TimeUnit.HOURS.toMinutes(1)
        var secLeft =
            TimeUnit.MILLISECONDS.toSeconds(durationLeft) % TimeUnit.MINUTES.toSeconds(1)

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

    private fun isCar(context: Context): Boolean {
        var uiMode: UiModeManager =
            context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiMode.currentModeType == Configuration.UI_MODE_TYPE_CAR
    }

    private val timerPlayerGetSynTask = object : Runnable {
        override fun run() {

            viewModel.getSyncPlayBackPosition(extraCloudBookId.toInt())
            mainHandler.postDelayed(this, 1000)
        }
    }

    private fun subscribeUi(binding: FragmentAudioPlayerBinding) {
        var chapterBookTxt = activity?.findViewById<TextView>(R.id.titleBook)
        //check come from button option
        if (fragmentStatus == "onAttach") {
            fragmentStatus = "onViewCreated"

            viewModel.listChapterResult.observe(viewLifecycleOwner, Observer { result ->

                //send first chapter and part
                viewModel.playAudioBook(
                    extraCloudBookId.toInt(),
                    result[0].chapter,
                    extraContentID,
                    extraLicenseId,
                    result[0].part
                )

                //sum book duration if first time
                if (viewModel.getBookTotalDuration(extraContentID.toInt()) == 0L) {
                    var totalBookDuration = 0L
                    for (chapter in result) {
                        totalBookDuration += chapter.duration
                    }
                    viewModel.saveBookSize(extraContentID.toInt(), result.size)
                    viewModel.saveBookTotalDuration(extraContentID.toInt(), totalBookDuration)
                }

            })
        }

        viewModel.getPlayBackPositionResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    if (result.data?.playback_position != null) {

                        println("SUCCESS get syn")
                        println("###")

                        viewModel.saveCurrentPart(
                            extraContentID.toInt(),
                            result.data.playback_position.part_number
                        )

                        viewModel.saveCurrentChapter(
                            extraContentID.toInt(),
                            result.data.playback_position.chapter
                        )

                        viewModel.savePositionPlay(
                            result.data.playback_position.position_in_millisecond.toLong(),
                            extraContentID.toInt(),
                            result.data.playback_position.chapter
                        )
                    }
                }

                Result.Status.ERROR -> {
                }
            }
        })

        viewModel.sessionData.observe(viewLifecycleOwner, Observer { result ->
            if (result.data?.account_id != null) {
                sessionId = result.data.key
                engineAccount = result.data.account_id
            }

        })

        viewModel.playBackResult.observe(viewLifecycleOwner, Observer { result ->
            if (currentPlayChapter != result.chapter!!.chapter) {
                currentPlayChapter = result.chapter!!.chapter
                Log.d("TAG", "subscribeUi: " + currentPlayChapter!!.toLong())
                remoteMediaClient?.seek(currentPlayChapter!!.toLong())
            }
            currentPart = result.chapter!!.part
            currentPlayPart = result.chapter!!.part
            chapterBookTxt?.text = "Chapter $currentPlayChapter"
            viewModel.saveCurrentChapter(extraContentID.toInt(), currentPlayChapter!!)
            updateMetadata(result)

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

                    viewModel.saveBookDuration(
                        extraContentID.toInt(),
                        result.chapter!!.duration
                    )

                    viewModel.saveCurrentPart(
                        extraContentID.toInt(),
                        result.chapter!!.part
                    )

                    viewModel.saveCurrentChapter (
                        extraContentID.toInt(),
                        result.chapter!!.chapter
                    )

                    viewModel.savePositionPlay(
                        result.position!!,
                        extraContentID.toInt(),
                        result.chapter!!.chapter
                    )
                }

                //need fix when play done
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
                    convertAndUpdateTime(
                        viewModel.getSavePositionPlay(extraContentID.toInt(),viewModel.getSaveBookCurrentChapter(extraContentID.toInt())),
                        viewModel.getBookDuration(extraContentID.toInt()),
                        binding
                    )
                    binding.playButton.setImageResource(R.drawable.play_arrow96)
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

