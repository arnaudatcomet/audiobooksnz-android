package com.audiobookz.nz.app.audiobookList.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.databinding.ListItemAudiobookBinding

class AudiobookListAdapter : ListAdapter<Audiobook, AudiobookListAdapter.ViewHolder>((DiffCallback()))
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudiobookListAdapter.ViewHolder {
        return ViewHolder(
            ListItemAudiobookBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audiobook = getItem(position)

        holder.apply {
            bind(audiobook)
            itemView.tag = audiobook
        }
    }
    class ViewHolder(private val binding: ListItemAudiobookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind( item: Audiobook) {
            binding.apply {
                audiobook = item
                executePendingBindings()
            }
        }
    }

}
private class DiffCallback : DiffUtil.ItemCallback<Audiobook>() {
    override fun areItemsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
        return oldItem == newItem
    }
}