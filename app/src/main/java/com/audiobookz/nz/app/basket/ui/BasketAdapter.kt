package com.audiobookz.nz.app.basket.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.databinding.ListItemBasketBinding
import com.audiobookz.nz.app.databinding.ListItemCategoriesBinding


class BasketAdapter : ListAdapter<BookRoom, BasketAdapter.ViewHolder>(DiffCallback())
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val BookRoom = getItem(position)
        holder.apply {
            bind(BookRoom)
            itemView.tag = BookRoom
        }
    }
    class ViewHolder(
        private val binding: ListItemBasketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookRoom) {
            binding.apply {
                binding.book = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBasketBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }
}
public class DiffCallback : DiffUtil.ItemCallback<BookRoom>() {
    override fun areItemsTheSame(oldItem: BookRoom, newItem: BookRoom): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookRoom, newItem: BookRoom): Boolean {
        return oldItem == newItem
    }
}