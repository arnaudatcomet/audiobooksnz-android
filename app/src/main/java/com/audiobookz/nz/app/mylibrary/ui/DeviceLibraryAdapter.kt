package com.audiobookz.nz.app.mylibrary.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.databinding.ListItemDeviceBinding
import com.audiobookz.nz.app.mylibrary.data.LocalBookData
import com.audiobookz.nz.app.player.ui.PlayerActivity

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class DeviceLibraryAdapter(private val activity: Context) :
    ListAdapter<LocalBookData, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<LocalBookData>() {
        override fun areItemsTheSame(oldItem: LocalBookData, newItem: LocalBookData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LocalBookData, newItem: LocalBookData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemDeviceBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBook = getItem(position)
        val itemBinding = holder.binding as ListItemDeviceBinding
        itemBinding.localBook = currentBook
        itemBinding.authors = currentBook.authors


        itemBinding.clickDirectPlay = directPlay(
            currentBook.id,
            currentBook.bookId!!,
            currentBook.contentId.toString(),
            currentBook.title!!,
            currentBook.image_url!!,
            currentBook.licenseId!!,
            currentBook.authors!!,
            currentBook.narrators!!
        )

        itemBinding.clickDeleteListener = currentBook.narrators?.let {
            openToDownload(
                currentBook.title!!,
                currentBook.image_url!!,
                currentBook.id,
                currentBook.bookId!!,
                currentBook.contentId.toString(),
                currentBook.licenseId!!,
                it,
                currentBook.authors!!
            )
        }

        itemBinding.executePendingBindings()
    }

    private fun directPlay(
        id: Int,
        bookId: String,
        contentId: String,
        title: String,
        url: String,
        licenseId: String,
        authors: String,
        narrators: String
    ): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(activity, PlayerActivity::class.java).apply {
                putExtra("cloudBookId", id)
                putExtra("bookId", bookId)
                putExtra("contentId", contentId)
                putExtra("urlImage", url)
                putExtra("titleBook", title)
                putExtra("licenseIDBook", licenseId)
                putExtra("authorBook", authors)
                putExtra("narratorBook", narrators)
            }
            activity.startActivity(intent)
        }
    }

    private fun openToDownload(
        title: String,
        imageUrl: String,
        id: Int,
        bookId: String,
        contentId: String,
        licenseId: String,
        narrators: String,
        authors: String
    ): View.OnClickListener {
        return View.OnClickListener {
            val direction = MyLibraryFragmentDirections.actionMylibraryToBookDownloadFragment(
                title,
                licenseId,
                id.toString(),
                bookId,
                contentId,
                authors,
                narrators,
                imageUrl
            )
            it.findNavController().navigate(direction)
        }
    }
}