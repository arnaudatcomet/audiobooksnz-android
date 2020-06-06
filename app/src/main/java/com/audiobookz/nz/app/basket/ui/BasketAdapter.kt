package com.audiobookz.nz.app.basket.ui

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.databinding.ListItemBasketBinding


class BasketAdapter(val viewModel: BasketViewModel) : ListAdapter<BookRoom, BasketAdapter.ViewHolder>(DiffCallback())
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val BookRoom = getItem(position)
        holder.apply {
            BookRoom.id?.let { createOnRemoveBookListener(it) }?.let { bind(it,BookRoom) }
            itemView.tag = BookRoom
        }
    }
    class ViewHolder(
        private val binding: ListItemBasketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(remove: View.OnClickListener,item: BookRoom) {
            binding.apply {
                binding.book = item
                binding.remove = remove
            }
        }
    }
    private fun createOnRemoveBookListener(id: Int): View.OnClickListener {
        return View.OnClickListener {
            AsyncTask.execute {
                viewModel.deleteCartById(id)
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