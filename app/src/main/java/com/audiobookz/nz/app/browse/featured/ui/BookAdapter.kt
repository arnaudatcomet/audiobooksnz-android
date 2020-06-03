package com.audiobookz.nz.app.browse.featured.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.databinding.ListItemFeaturedBookBinding

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class BookAdapter : ListAdapter<Audiobook, CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Audiobook>() {
        override fun areItemsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
            return  oldItem.id == newItem.id
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
        itemBinding.audioFeatured = currentBook
        itemBinding.executePendingBindings()
    }
}