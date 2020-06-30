package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentPlayerChapterBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.setTitle
import javax.inject.Inject

class PlayerChapterFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    lateinit var extraID: String
    lateinit var extraLicenseId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        var binding = FragmentPlayerChapterBinding.inflate(inflater, container, false)
        var titleBook = activity?.findViewById<TextView>(R.id.titleBook)
        extraID = activity?.intent?.getStringExtra("idBook").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        titleBook?.text = ""
        setTitle("Chapter")
        viewModel.getChapters(extraID)

        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentPlayerChapterBinding) {
        viewModel.listChapterResult.observe(viewLifecycleOwner, Observer { result ->
            var currentChapter = viewModel.currentPlay?.chapter
            val adapter =
                currentChapter?.let { PlayerChapterAdapter(it, viewModel, extraID,extraLicenseId) }
            adapter?.submitList(result)
            binding.chapterRecycleView.adapter = adapter

        })
    }

}
