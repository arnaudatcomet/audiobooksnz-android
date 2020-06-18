package com.audiobookz.nz.app.library.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.databinding.FragmentLibraryBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import io.audioengine.mobile.DownloadEvent
import javax.inject.Inject


class FragmentLibrary : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LibraryViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentLibraryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.openPlayer = delete()
        binding.download = download()
        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentLibraryBinding) {
        viewModel.downloadResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.code) {
                DownloadEvent.DOWNLOAD_PROGRESS_UPDATE -> {
                    binding.status = "Downloading"
                    binding.chapter = result.chapterPercentage
                    if (result.contentPercentage != 0) {
                        binding.content = result.contentPercentage
                    }
                }
                DownloadEvent.DOWNLOAD_STARTED -> {
                    if(!binding.status.equals("Downloading"))
                    {
                        binding.status = "Starting"
                    }
                }
                DownloadEvent.DOWNLOAD_PAUSED -> {
                    binding.status = "Paused"
                }
                DownloadEvent.CONTENT_DOWNLOAD_COMPLETED -> {
                    binding.status = "Completed"
                }
            }
        })
    }

    private fun download(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.download("the best")
        }
    }

    private fun delete(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.delete()
        }
    }

}