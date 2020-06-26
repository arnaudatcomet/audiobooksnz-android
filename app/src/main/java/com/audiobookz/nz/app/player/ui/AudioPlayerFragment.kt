package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentAudioPlayerBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.setTitle
import io.audioengine.mobile.PlaybackEvent
import javax.inject.Inject


class AudioPlayerFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    lateinit var extraID:String
    lateinit var extraLicenseId:String
    var statePlay = true
    var currentPosition : Long = 0
    var speedEngine:Float = 1F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        extraID = activity?.intent?.getStringExtra("idBook").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()

        activity?.intent?.getStringExtra("titleBook")?.let { setTitle(it) }
        binding.urlImage = activity?.intent?.getStringExtra("urlImage")
        binding.speedSelectClick = speedSelectClick()
        binding.chapterSelectClick = chapterSelectClick()
        binding.sleepTimeSelectClick = sleepTimeClick()
        binding.addToBookmarkClick = addBookmarkClick()
        binding.skipPreviousClick = skipPreviousClick()
        binding.skipNextClick = skipNextClick()
        binding.forward30sClick = forward30sClick()
        binding.replay30sClick = replay30sClick()
        binding.playClick = playClick(binding)
        viewModel.getChapters(extraID)
        subscribeUi(binding)
        viewModel.getPlayerState()
        return binding.root
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

    private fun playClick(binding: FragmentAudioPlayerBinding): View.OnClickListener {
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
            currentPosition = viewModel.getPosition()
            viewModel.seekTo( currentPosition + 30000)
        }
    }

    private fun replay30sClick(): View.OnClickListener {
        return View.OnClickListener {
            currentPosition = viewModel.getPosition()
            viewModel.seekTo( currentPosition - 30000)
        }
    }

    private fun speedSelectClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction = AudioPlayerFragmentDirections.actionAudioPlayerFragmentToPlayerSpeedFragment(speedEngine)
            it.findNavController().navigate(direction)
        }
    }

    private fun chapterSelectClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction = AudioPlayerFragmentDirections.actionAudioPlayerFragmentToChapterFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun sleepTimeClick(): View.OnClickListener {
        return View.OnClickListener {
            val direction = AudioPlayerFragmentDirections.actionAudioPlayerFragmentToPlayerSleepTimeFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun addBookmarkClick(): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    fun convertAndUpdateTime(position:Long, duration:Long, binding: FragmentAudioPlayerBinding){

        var totalDuration = duration / 1000

        var spentTime = position/ 1000
        var leftTime = (totalDuration - spentTime).toInt()
        var hourLeft = (leftTime / 3600)
        var minLeft = ((leftTime / 60) %60)
        var secLeft = (leftTime % 60)

        var hourSpent = (spentTime / 3600).toInt()
        var minSpent = ((spentTime / 60) %60).toInt()
        var secSpent = (spentTime % 60).toInt()

        //need fix seekbar progress and duration text
        binding.progressPlayerTimePlay.progress = secSpent
        binding.progressPlayerTimePlay.max = totalDuration.toInt()

        if (hourLeft != 0) {
            binding.timeLeftTxt.text = "$hourLeft:$minLeft:$secLeft"
        }
        else{
            binding.timeLeftTxt.text = "$minLeft:$secLeft"
        }

        if (hourSpent == 0) {
            binding.timeSpentTxt.text = "$minSpent:$secSpent"
        }
        else{
            binding.timeSpentTxt.text = "$hourSpent:$minSpent:$secSpent"
        }

    }

    private fun subscribeUi(binding: FragmentAudioPlayerBinding) {
        viewModel.listChapterResult.observe(viewLifecycleOwner, Observer { result ->
            viewModel.playAudioBook(result[0].chapter, extraID, extraLicenseId, result[0].part)
            //recycleview adapter for chapter fragment
        })

        viewModel.playBackResult.observe(viewLifecycleOwner, Observer { result ->
            var titleBook = activity?.findViewById<TextView>(R.id.titleBook)
            titleBook?.text = "Chapter ${result.chapter?.chapter}"
            speedEngine = result.speed!!

           when(result.code){
               PlaybackEvent.PLAYBACK_PROGRESS_UPDATE ->{
                   result.position?.let { result.duration?.let { it1 -> convertAndUpdateTime(it, it1, binding) } }

               }
           }
        })

        viewModel.playerStateResult.observe(viewLifecycleOwner, Observer { result ->
           when(result.name){
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
    }

}
