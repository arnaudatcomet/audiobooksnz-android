package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.bookdetail.ui.BookDetailViewModel
import com.audiobookz.nz.app.bookdetail.ui.ReviewsFragment
import com.audiobookz.nz.app.bookdetail.ui.SummaryFragment
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBookDownloadBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
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
        viewModel.getContentStatus(args.id)

        return binding.root
    }

    private fun downloadAndDeleteButton(binding: FragmentBookDownloadBinding): View.OnClickListener {
        return View.OnClickListener {

            when (binding.bookStatus) {
                "Download" -> {
                    binding.downloadStatus = "Pending"
                    viewModel.download( args.id, args.licenseId)
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
                   viewModel.download( args.id, args.licenseId)
                }
                "Delete" -> {
                    viewModel.deleteContent(args.id, args.licenseId)
                    binding.downloadStatus = "status"
                }
            }
        }
    }

    private fun subscribeUi(binding: FragmentBookDownloadBinding) {

        viewModel.bookDetail.observe(viewLifecycleOwner, Observer { result->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.downloadStatus = "Completed"
                    binding.contentProcess = 100
                    binding.progressDownload.isIndeterminate = false

                }
            }
        })

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
                    binding.downloadStatus = "Fetching information"
                    viewModel.getDetailBook(args.apiBookId.toString(),args.title?:"Book")
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
                        binding.downloadStatus ="Downloading"
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
