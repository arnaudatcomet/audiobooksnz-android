package com.audiobookz.nz.app.more.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.bookdetail.ui.CustomViewHolder
import com.audiobookz.nz.app.databinding.ListItemCardBinding

import com.audiobookz.nz.app.more.data.CardDetailData
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CardListAdapter(private var viewModel: MoreViewModel, private var defaultCard: String) :
    ListAdapter<CardDetailData, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<CardDetailData>() {
        override fun areItemsTheSame(oldItem: CardDetailData, newItem: CardDetailData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CardDetailData, newItem: CardDetailData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCardBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        val itemBinding = holder.binding as ListItemCardBinding

        itemBinding.card = currentItem
        itemBinding.isDefault = currentItem.id == defaultCard

        itemBinding.remove = removeList(currentItem.id)
        itemBinding.setCardDefault = setDefaultCard(currentItem.id)

    }

    private fun removeList(cardId: String): View.OnClickListener {
        return View.OnClickListener {
            viewModel.removeCardList(cardId)
        }
    }

    private fun setDefaultCard(cardId: String): View.OnClickListener {
        return View.OnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle("Set Default Card")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.setDefaltCard(cardId)
                }
                .setNegativeButton("No"){ _, _ ->
                }
                .show()

        }
    }


}