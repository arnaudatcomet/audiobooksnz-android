package com.audiobookz.nz.app.mylibrary.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.databinding.FragmentBookDownloadBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.player.ui.PlayerActivity
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadStatus
import javax.inject.Inject

class BookDownloadFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyLibraryViewModel
    private val args: BookDownloadFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentBookDownloadBinding.inflate(inflater, container, false)
        binding.nameBookDownload = args.title
        binding.urlImage = args.url

        binding.statusButton = downloadAndDeleteButton(binding)
        subscribeUi(binding)

        //need check status first
        binding.bookStatus = "Download"
        binding.downloadStatus = "Status"
        binding.goToPlayerClick = goToPlayerClick(binding)
        viewModel.getContentStatus(args.id)

        return binding.root
    }

    private fun downloadAndDeleteButton(binding: FragmentBookDownloadBinding): View.OnClickListener {
        return View.OnClickListener {

            when (binding.bookStatus) {
                "Download" -> {
                    binding.downloadStatus = "Pending"
                    args.title?.let { it1 -> viewModel.download(it1, args.id, args.licenseId) }
                }
                "Queued" -> {

                }
                "Cancel" -> {
                    binding.bookStatus = "Download"
                    binding.downloadStatus = "Status"
                    viewModel.deleteContent(args.id, args.licenseId)
                    binding.progressDownload.isIndeterminate = false
                }
                "Paused" -> {
                    args.title?.let { it1 -> viewModel.download(it1, args.id, args.licenseId) }
                }
                "Delete" -> {
                    viewModel.deleteContent(args.id, args.licenseId)
                    binding.downloadStatus = "status"
                }
            }
        }
    }

    private fun goToPlayerClick(binding: FragmentBookDownloadBinding): View.OnClickListener {
        return View.OnClickListener {
            if (binding.downloadStatus == "Completed") {
                val intent = Intent(activity, PlayerActivity::class.java).apply {
                    putExtra("idBook", args.id)
                    putExtra("urlImage", args.url)
                    putExtra("titleBook", args.title)
                    putExtra("licenseIDBook", args.licenseId)
                }
                startActivity(intent)
            }
        }
    }

    private fun subscribeUi(binding: FragmentBookDownloadBinding) {
        viewModel.downloadResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.code) {
                DownloadEvent.DOWNLOAD_PROGRESS_UPDATE -> {
                    binding.downloadStatus = "Downloading"
                    if (result.contentPercentage != 0) {
                        binding.contentProcess = result.contentPercentage
                    }
                }
                DownloadEvent.DOWNLOAD_STARTED -> {
                    if (binding.downloadStatus != "Downloading") {
                        binding.downloadStatus = "Starting"
                    }
                }
                DownloadEvent.DOWNLOAD_PAUSED -> {
                    binding.downloadStatus = "Paused"
                    binding.contentProcess = result.contentPercentage
                }
                DownloadEvent.CONTENT_DOWNLOAD_COMPLETED -> {
                    binding.downloadStatus = "Completed"
                    binding.contentProcess = 100
                    binding.progressDownload.isIndeterminate = false

                }
            }
        })
        viewModel.contentStatusResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                DownloadStatus.NOT_DOWNLOADED -> {
                    binding.bookStatus = "Download"
                    binding.contentProcess = 0

                }
                DownloadStatus.QUEUED -> {
                    binding.bookStatus = "Queued"
                }
                DownloadStatus.DOWNLOADING -> {
                    binding.bookStatus = "Cancel"
                    if (binding.downloadStatus == "Status") {
                        binding.downloadStatus = "Downloading"
                        binding.percentTxt.text = ""
                        binding.progressDownload.isIndeterminate = true
                    }
                }
                DownloadStatus.PAUSED -> {
                }
                DownloadStatus.DOWNLOADED -> {
                    binding.bookStatus = "Delete"
                    binding.downloadStatus = "Completed"
                    binding.contentProcess = 100
                    binding.progressDownload.isIndeterminate = false

                }
            }
        })
    }

}
