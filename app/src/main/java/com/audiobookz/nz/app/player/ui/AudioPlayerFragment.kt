package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.PopupMenu

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentAudioPlayerBinding

class AudioPlayerFragment : Fragment() {
    var statePlay = "Play"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)

        binding.speedSelectClick = speedSelectClick()
        binding.chapterSelectClick = chapterSelectClick()
        binding.sleepTimeSelectClick = sleepTimeClick()
        binding.addToBookmarkClick = addBookmarkClick()
        binding.skipPreviousClick = skipPreviousClick()
        binding.skipNextClick = skipNextClick()
        binding.forward30sClick = forward30sClick()
        binding.replay30sClick = replay30sClick()
        binding.playClick = playClick(binding)

        return binding.root
    }

    private fun skipPreviousClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun skipNextClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun playClick(binding: FragmentAudioPlayerBinding): View.OnClickListener {
        return View.OnClickListener {
            if (statePlay == "Play"){
                statePlay = "Pause"
                binding.playButton.setImageResource(R.drawable.pause96)
            }else{
                statePlay = "Play"
                binding.playButton.setImageResource(R.drawable.play_arrow96)
            }
        }}

    private fun forward30sClick(): View.OnClickListener {
        return View.OnClickListener {

        }}
    private fun replay30sClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun speedSelectClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun chapterSelectClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun sleepTimeClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

    private fun addBookmarkClick(): View.OnClickListener {
        return View.OnClickListener {

        }}

}
