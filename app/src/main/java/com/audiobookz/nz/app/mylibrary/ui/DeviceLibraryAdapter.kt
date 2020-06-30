package com.audiobookz.nz.app.mylibrary.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.databinding.FragmentDeviceLibraryfragmentBinding
import com.audiobookz.nz.app.databinding.ListItemDeviceBinding
import com.audiobookz.nz.app.mylibrary.data.LocalBookData

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class DeviceLibraryAdapter  : ListAdapter<LocalBookData, CustomViewHolder>(Companion) {

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
        itemBinding.authors = currentBook.audioengine_data.copyright.authors.joinToString(separator = ",")

        //click event
//        itemBinding.clickDirectPlay =
//            currentBook.audiobook?.id?.let { directPlay(it, currentBook.audiobook.title) }
//        itemBinding.clickDeleteListener = openToDownload(currentBook.title, currentBook.)

        itemBinding.executePendingBindings()
    }

//    private fun directPlay(id: Int, title: String): View.OnClickListener {
//        return View.OnClickListener {
//            val direction =
//                BrowseFragmentDirections.actionBrowseFragmentToBookDetailFragment(id, title)
//            it.findNavController().navigate(direction)
//        }
//    }

//    private fun openToDownload(title: String, url: String,id: String,licenseId:String,apiBookId:Int): View.OnClickListener {
//        return View.OnClickListener {
//            val direction = MyLibraryFragmentDirections.actionMylibraryToBookDownloadFragment(title,licenseId,apiBookId, id,url)
//            it.findNavController().navigate(direction)
//        }
//    }
}