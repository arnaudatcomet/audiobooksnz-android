package com.audiobookz.nz.app.mylibrary.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.ListItemCloudBinding
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.util.intentShareText

class CloudLibraryAdapter(
    private val context: Activity,
    private val resultDataLibrary: Map<String, List<Any>>,
    private val viewModel: MyLibraryViewModel
) :
    RecyclerView.Adapter<CloudLibraryAdapter.ViewHolder>() {
    var bookDuration = 0
    var resultCalculate = 0
    var timeRemainingText = ""


    override fun getItemCount(): Int {
        return resultDataLibrary["cloudList"]?.size ?: 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCloudBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cloudBookList = resultDataLibrary["cloudList"] as List<CloudBook>
        val cloudBook = cloudBookList?.get(position) as CloudBook
        val bookTitle = cloudBook.audiobook?.title
        var bookContentID = cloudBook.audiobook?.audioengine_audiobook_id
        val licenseId = cloudBook.audioengineLicenseData?.licenses?.get(0)?.id
        var resultCheckDownload = checkDownloaded(cloudBook)
        val authors = cloudBook.audiobook?.audioengine_data?.BookDetail?.authors?.joinToString(",")
        val narrators =
            cloudBook.audiobook?.audioengine_data?.BookDetail?.narrators?.joinToString(",")

        if (resultCheckDownload) {

            bookDuration = viewModel.getBookDuration(bookContentID!!.toInt()).toInt()
            resultCalculate = viewModel.calculateRemainingTime(bookContentID!!.toInt())
            timeRemainingText = viewModel.timeRemaining
        }

        holder.apply {
            bookTitle?.let {
                openToDownload(
                    it,
                    cloudBook.audiobook.cover_image,
                    cloudBook.id.toString(),
                    cloudBook.audiobook.id.toString(),
                    bookContentID!!,
                    licenseId!!,
                    narrators!!,
                    authors!!
                )
            }?.let {
                bind(
                    cloudBook, it, openOptionMenu(cloudBook.audiobook.id, bookTitle,authors!!,narrators!!),
                    resultCheckDownload, bookDuration, resultCalculate, timeRemainingText
                )
            }
        }
        holder.itemView.tag = cloudBook
    }

    class ViewHolder(private val binding: ListItemCloudBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CloudBook,
            openDownloadListener: View.OnClickListener,
            openOptionListener: View.OnClickListener,
            isDownload: Boolean,
            duration: Int,
            remainingTime: Int,
            remainingText: String
        ) {

            binding.apply {

                cloudBook = item
                isDownloaded = isDownload
                authors =
                    item.audiobook?.audioengine_data?.BookDetail?.authors?.joinToString(separator = ",")
                progressTimeRemain.max = duration
                contentProcess = remainingTime
                timeRemaining = "$remainingText remaining"
                clickDownloadListener = openDownloadListener
                clickOptionMenuListener = openOptionListener
                executePendingBindings()
            }
        }
    }

    private fun openToDownload(title: String, imageUrl: String, cloudBookId: String, bookId: String, contentId: String, licenseId: String, narrators: String, authors: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = MyLibraryFragmentDirections.actionMylibraryToBookDownloadFragment(
                title,
                licenseId,
                cloudBookId,
                bookId,
                contentId,
                authors,
                narrators,
                imageUrl
            )
            it.findNavController().navigate(direction)
        }
    }

    private fun openOptionMenu(id: Int, title: String, authors: String, narrators: String): View.OnClickListener {
        return View.OnClickListener {
            val pop = PopupMenu(it.context, it)
            pop.inflate(R.menu.cloud_library_option)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.detail -> {
                        val direction =
                            MyLibraryFragmentDirections.actionMylibraryToBookDetailFragment(
                                id,
                                title,
                                false
                            )
                        it.findNavController().navigate(direction)
                    }
                    R.id.review -> {
                        val direction =
                            MyLibraryFragmentDirections.actionMylibraryToRateAndReviewFragment(id,authors,narrators,title)
                        it.findNavController().navigate(direction)
                    }
                    R.id.share -> {
                        intentShareText(
                            context, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                            "testShare"
                        )
                    }
                };true
            }
            pop.show()
        }
    }

    private fun checkDownloaded(cloudBook: CloudBook): Boolean {
        val localBookList = resultDataLibrary["localList"] as List<String>?

        if (localBookList != null) {
            for (contentId in localBookList) {
                if (cloudBook.audiobook?.audioengine_audiobook_id == contentId) {
                    return true
                }
            }
        }

        return false
    }

}
