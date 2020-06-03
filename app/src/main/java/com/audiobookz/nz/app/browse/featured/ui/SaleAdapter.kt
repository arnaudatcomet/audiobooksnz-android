package com.audiobookz.nz.app.browse.featured.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.databinding.ListItemFeaturedBinding

class SaleAdapter : ListAdapter<Featured, CustomViewHolder>(Companion) {
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object : DiffUtil.ItemCallback<Featured>() {
        override fun areItemsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return  oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFeaturedBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBookCategory = getItem(position)
        val itemBinding = holder.binding as ListItemFeaturedBinding

        itemBinding.featuredData = currentBookCategory
        itemBinding.nestRecyclerView.setRecycledViewPool(viewPool)
        itemBinding.executePendingBindings()


    }
}