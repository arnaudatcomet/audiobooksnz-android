package com.audiobookz.nz.app.bookdetail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.databinding.ListReviewBinding

class CustomViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

class ReviewAdapter : ListAdapter<BookReview, CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<BookReview>() {
        override fun areItemsTheSame(oldItem: BookReview, newItem: BookReview): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BookReview, newItem: BookReview): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListReviewBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBook = getItem(position)
        val itemBinding = holder.binding as ListReviewBinding
        itemBinding.txtReviewerName.text = currentBook.createdBy.first_name +" "+ currentBook.createdBy.last_name
        itemBinding.reviewComment.text = currentBook.comment
        itemBinding.ratingStartReview.rating = currentBook.story_rating.toFloat()

    }

}