package com.audiobookz.nz.app.mylibrary.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.ListItemCloudBinding
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.util.intentShareText
import java.nio.file.OpenOption

class CloudLibraryAdapter(private val context: Activity) :
    ListAdapter<CloudBook, CloudLibraryAdapter.ViewHolder>((DiffCallback())) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CloudLibraryAdapter.ViewHolder {
        return ViewHolder(
            ListItemCloudBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    private fun openToDownload(title: String, url: String,id: String,licenseId:String): View.OnClickListener {
        return View.OnClickListener {
            val direction = MyLibraryFragmentDirections.actionMylibraryToBookDownloadFragment(title,licenseId, id,url)
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
        val cloudBook = getItem(position)
        val bookTitle = cloudBook.audiobook?.title
        val licenseId = cloudBook.audioengineLicenseData?.licenses?.get(0)?.id


        holder.apply {
            bookTitle?.let { openToDownload(it, cloudBook.audiobook.cover_image,cloudBook.audiobook.audioengine_audiobook_id,licenseId!!) }?.let {
                bind(cloudBook,
                    it, openOptionMenu(cloudBook.audiobook.id, bookTitle),true)
            }
            itemView.tag = cloudBook
        }
    }

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
}

private class DiffCallback : DiffUtil.ItemCallback<CloudBook>() {
    override fun areItemsTheSame(oldItem: CloudBook, newItem: CloudBook): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CloudBook, newItem: CloudBook): Boolean {
        return oldItem.id == newItem.id
    }
}