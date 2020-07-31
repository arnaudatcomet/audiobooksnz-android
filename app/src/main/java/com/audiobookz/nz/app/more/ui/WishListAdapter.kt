package com.audiobookz.nz.app.more.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.audiobookz.nz.app.bookdetail.ui.CustomViewHolder
import com.audiobookz.nz.app.databinding.ListItemWishlistBinding
import com.audiobookz.nz.app.more.data.WishListData


class WishListAdapter(private var viewModel: MoreViewModel) :
    ListAdapter<WishListData, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<WishListData>() {
        override fun areItemsTheSame(oldItem: WishListData, newItem: WishListData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WishListData, newItem: WishListData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemWishlistBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        val itemBinding = holder.binding as ListItemWishlistBinding
        var bookId = currentItem.audiobook_id
        var booktitle = currentItem.audiobook?.title
        itemBinding.book = currentItem.audiobook
        itemBinding.remove = removeList(bookId)
        itemBinding.bookDetail = goToBookDetail(bookId,booktitle)

    }

    private fun removeList(bookId: Int): View.OnClickListener {
        return View.OnClickListener {
            viewModel.removeWishList(bookId)
        }
    }

    private fun goToBookDetail(bookId: Int, booktitle: String?): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                WishListFragmentDirections.actionWishlistFragmentToBookDetailFragment(
                    bookId,
                    booktitle
                )
            it.findNavController().navigate(direction)
        }
    }
}