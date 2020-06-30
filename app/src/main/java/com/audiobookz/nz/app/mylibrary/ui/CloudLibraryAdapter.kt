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

class CloudLibraryAdapter(private val context: Activity, private val resultDataLibrary: Map<String, List<Any>>) :
    RecyclerView.Adapter<CloudLibraryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListItemCloudBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: CloudBook,
            openDownloadListener: View.OnClickListener,
            openOptionListener: View.OnClickListener,
            isDownload: Boolean
        ) {
            binding.apply {
                cloudBook = item
                isDownloaded = isDownload
                authors = item.audiobook?.audioengine_data?.BookDetail?.authors?.joinToString(separator = ",")
                clickDownloadListener = openDownloadListener
                clickOptionMenuListener = openOptionListener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCloudBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private fun openToDownload(title: String, imageUrl: String,id: String,licenseId:String,apiBookId:Int,narrators:String,authors:String): View.OnClickListener {
        return View.OnClickListener {
            val direction = MyLibraryFragmentDirections.actionMylibraryToBookDownloadFragment(
                title,
                licenseId,
                apiBookId,
                id,
                authors,
                narrators,
                imageUrl
            )
            it.findNavController().navigate(direction)
        }
    }

    private fun openOptionMenu(id: Int, title: String): View.OnClickListener {
        return View.OnClickListener {
            val pop = PopupMenu(it.context, it)
            pop.inflate(R.menu.cloud_library_option)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.detail -> {
                        val direction =
                            MyLibraryFragmentDirections.actionMylibraryToBookDetailFragment(
                                id,
                                title
                            )
                        it.findNavController().navigate(direction)
                    }
                    R.id.review -> {
//                        val direction =
//                            MyLibraryFragmentDirections.actionMylibraryToRateAndReviewFragment(id)
//                        it.findNavController().navigate(direction)
                    }
                    R.id.share -> {
                        intentShareText( context, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                            "testShare")
                    }
                };true
            }
            pop.show()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val cloudBookList =  resultDataLibrary["cloudList"]

        val cloudBook = cloudBookList?.get(position) as CloudBook
        val bookTitle = cloudBook.audiobook?.title
        val licenseId = cloudBook.audioengineLicenseData?.licenses?.get(0)?.id
        var resultCheckDownload = checkDownloaded(cloudBook)
        val authors = cloudBook.audiobook?.audioengine_data?.BookDetail?.authors?.joinToString(",")
        val narrators = cloudBook.audiobook?.audioengine_data?.BookDetail?.narrators?.joinToString(",")

        holder.apply {
            bookTitle?.let {
                openToDownload(
                    it,
                    cloudBook.audiobook.cover_image,
                    cloudBook.audiobook.audioengine_audiobook_id,
                    licenseId!!,
                    cloudBook.audiobook_id,
                    narrators!!,
                    authors!!
                )
            }?.let {
                bind(cloudBook, it, openOptionMenu(cloudBook.audiobook.id, bookTitle), resultCheckDownload)
            }
        }
        holder.itemView.tag = cloudBook
    }

    override fun getItemCount(): Int {
        return resultDataLibrary["cloudList"]?.size ?: 1
    }

    private fun checkDownloaded(cloudBook: CloudBook):Boolean{
        val localBookList = resultDataLibrary["localList"] as List<String>?

        if (localBookList != null) {
            for (bookId in localBookList){
                if (cloudBook.audiobook?.audioengine_audiobook_id == bookId){
                    return true
                }
            }
        }

        return false
    }

}
