package com.audiobookz.nz.app.basket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.databinding.ListItemOrderBinding

class OrderAdapter(): ListAdapter<BookRoom, OrderAdapter.ViewHolder>(DiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookRoom = getItem(position)
        holder.apply {
            bind(bookRoom)
            itemView.tag = bookRoom
        }
    }
    class ViewHolder(
        private val binding: ListItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookRoom) {
            binding.apply {
                binding.book = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }
}
