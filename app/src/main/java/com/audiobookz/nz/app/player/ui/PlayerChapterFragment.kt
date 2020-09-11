package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    lateinit var extraContentId: String
    lateinit var extraLicenseId: String
    //lateinit var extraCloudId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        var binding = FragmentPlayerChapterBinding.inflate(inflater, container, false)
        extraContentId = activity?.intent?.getStringExtra("contentId").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        //extraCloudId = activity?.intent?.getStringExtra("cloudBookId").toString()
        setTitle("Chapter")
        viewModel.getChapters(extraContentId)
        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentPlayerChapterBinding) {
        viewModel.listChapterResult.observe(viewLifecycleOwner, Observer { result ->
            var currentChapter = 0
            if (viewModel.currentPlay?.contentId == extraContentId){
                currentChapter = viewModel.currentPlay!!.chapter
            }

            val adapter = PlayerChapterAdapter(viewModel,
                //extraCloudId.toInt(),
                extraContentId, extraLicenseId, currentChapter)
            adapter?.submitList(result)
            binding.chapterRecycleView.adapter = adapter
        })
    }

}
