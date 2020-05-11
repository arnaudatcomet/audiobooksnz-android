package com.audiobookz.nz.app.browse.ui

import androidx.recyclerview.widget.DiffUtil
import com.audiobookz.nz.app.browse.data.Category


private class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}