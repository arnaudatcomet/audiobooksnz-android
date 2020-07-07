package com.audiobookz.nz.app.player.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.databinding.ListItemChapterBinding
import io.audioengine.mobile.Chapter

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class PlayerChapterAdapter(

    private val viewModel: PlayerViewModel,
    private val id: String,
    private val licenseId: String,
    currentChapter: Int
) : ListAdapter<Chapter, CustomViewHolder>(Companion) {
    var oldChapter: Int = currentChapter

    companion object : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem.contentId == newItem.contentId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemChapterBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        val itemBinding = holder.binding as ListItemChapterBinding
        var totalDuration = currentItem.duration / 1000
        var hour = (totalDuration / 3600)
        var min = ((totalDuration / 60) % 60)
        var sec = (totalDuration % 60)

        itemBinding.listChapterData = currentItem

        if (hour != 0L) {
            itemBinding.duration = String.format("%02d:%02d:%02d",hour,min,sec)
        } else {
            itemBinding.duration = String.format("%02d:%02d",min,sec)
        }

        itemBinding.isClick = oldChapter == currentItem.chapter

        itemBinding.onClick = chooseChapter(currentItem,position)

        itemBinding.executePendingBindings()
    }

    private fun chooseChapter(currentItem:Chapter,
        position: Int
    ): View.OnClickListener {
        return View.OnClickListener {
            var chapter = currentItem.chapter
            var part = currentItem.part
            viewModel.chooseNewChapter(chapter, id, licenseId, part)
            oldChapter = currentItem.chapter
            notifyDataSetChanged()
        }
    }

}