package com.audiobookz.nz.app.audiobookList.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
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
    private fun createOnOpenBookDetailListener(id: Int, title: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = AudiobookListFragmentDirections.actionAudiobookListFragmentToBookDetailFragment(id,title,true)
            it.findNavController().navigate(direction)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audiobook = getItem(position)

        holder.apply {
            bind(audiobook,createOnOpenBookDetailListener(audiobook.id,audiobook.title))
            itemView.tag = audiobook
        }
    }
    class ViewHolder(private val binding: ListItemAudiobookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind( item: Audiobook,openBookDetailListener: View.OnClickListener) {
            binding.apply {
                audiobook = item
               // rateViewList.rating = item.avg_rating.toFloat()
                openBookDetail = openBookDetailListener
                executePendingBindings()
            }
        }
    }

}
private class DiffCallback : DiffUtil.ItemCallback<Audiobook>() {
    override fun areItemsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Audiobook, newItem: Audiobook): Boolean {
        return oldItem.id == newItem.id
    }
}