package com.audiobookz.nz.app.browse.featured.ui


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.databinding.ListItemCategoriesBinding
import com.audiobookz.nz.app.databinding.ListItemFeaturedBinding

class SaleAdapter : ListAdapter<Featured,SaleAdapter.ViewHolder>(DiffCallback())
{


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Featured = getItem(position)
        if(Featured.type_id!=3){
        holder.apply {
            bind(Featured)
            itemView.tag = Featured
        }
        }
    }
    class ViewHolder(
        private val binding: ListItemFeaturedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind( item: Featured) {
            if(item.type_id!=3){
            binding.apply {
                audioCategory = item.audiobook
                executePendingBindings()
            }
        }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemFeaturedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        //To change body of created functions use File | Settings | File Templates.
    }
}
public class DiffCallback : DiffUtil.ItemCallback<Featured>() {
    override fun areItemsTheSame(oldItem: Featured, newItem: Featured): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Featured, newItem: Featured): Boolean {
        return oldItem == newItem
    }
}