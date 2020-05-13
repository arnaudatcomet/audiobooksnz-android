package com.audiobookz.nz.app.browse.categories.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.databinding.ListItemCategoriesBinding

class CategoryAdapter : ListAdapter<Category,CategoryAdapter.ViewHolder>(DiffCallback())
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.apply {
            bind(createOnClickListener(category.id, category.title), category)
            itemView.tag = category
        }
    }
    private fun createOnClickListener(id: String, title: String): View.OnClickListener {
        return View.OnClickListener {
            Log.d("TAG", id+title)
        }
    }
    class ViewHolder(
        private val binding: ListItemCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Category) {
            binding.apply {
                clickListener = listener
                audioCategory = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCategoriesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
        //To change body of created functions use File | Settings | File Templates.
    }
}
private class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}