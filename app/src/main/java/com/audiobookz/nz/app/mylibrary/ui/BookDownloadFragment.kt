package com.audiobookz.nz.app.mylibrary.ui

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
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadStatus
import javax.inject.Inject
class BookDownloadFragment : Fragment(), Injectable  {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyLibraryViewModel
    var statusCheck = "Not Downloaded"
    private val args: BookDownloadFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentBookDownloadBinding.inflate(inflater, container, false)
        binding.nameBookDownload = args.title
        binding.urlImage = args.url

        binding.download = download(binding)
        binding.delete = detele()
        subscribeUi(binding)

        //need check status first
        binding.bookStatus = "Download"
        binding.downloadStatus = "Status"
        viewModel.getContentStatus(args.id)

        return binding.root
    }

    private fun detele(): View.OnClickListener? {
        return View.OnClickListener {
            viewModel.deleteContent(args.id,args.licenseId)
        }
    }

    private fun download(binding: FragmentBookDownloadBinding): View.OnClickListener {
        return View.OnClickListener {
            binding.downloadStatus = "Pending"
            args.title?.let { it1 -> viewModel.download(it1,args.id,args.licenseId) }
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
                    if(!binding.downloadStatus.equals("Downloading"))
                    {
                        binding.downloadStatus = "Starting"
                    }
                }
                DownloadEvent.DOWNLOAD_PAUSED -> {
                    binding.downloadStatus = "Paused"
                }
                DownloadEvent.CONTENT_DOWNLOAD_COMPLETED -> {
                    binding.downloadStatus = "Completed"
                }
            }
        })
        viewModel.contentStatusResult.observe(viewLifecycleOwner, Observer { result->
            when(result){
                DownloadStatus.NOT_DOWNLOADED ->{
                    binding.bookStatus = "download"
                }
                DownloadStatus.QUEUED ->{
                    binding.bookStatus = "QUEUED"
                }
                DownloadStatus.DOWNLOADING ->{
                    binding.bookStatus = "cancel"
                }
                DownloadStatus.PAUSED ->{
                    binding.bookStatus = "PAUSED"
                }
                DownloadStatus.DOWNLOADED ->{
                    binding.bookStatus = "delete"
                }
            }
        })
    }

}
