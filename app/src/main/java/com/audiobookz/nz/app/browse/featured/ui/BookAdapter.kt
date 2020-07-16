package com.audiobookz.nz.app.browse.featured.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.databinding.ListItemFeaturedBookBinding

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class BookAdapter : ListAdapter<Featured, CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Featured>() {
        override fun areItemsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFeaturedBookBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBook = getItem(position)
        val itemBinding = holder.binding as ListItemFeaturedBookBinding
        itemBinding.audioFeatured = currentBook.audiobook
        itemBinding.authorsName =
            currentBook.audiobook?.audioengine_data?.BookDetail?.authors?.joinToString(separator = ",")

        //click event
        itemBinding.clickListener =
            currentBook.audiobook?.id?.let { createOnOpenBookDetailListener(it, currentBook.audiobook.title) }

        itemBinding.executePendingBindings()
    }

    //navigation component browse fragment to bookDetail
    private fun createOnOpenBookDetailListener(id: Int, title: String): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                BrowseFragmentDirections.actionBrowseFragmentToBookDetailFragment(id, title, true)
            it.findNavController().navigate(direction)
        }
    }
}