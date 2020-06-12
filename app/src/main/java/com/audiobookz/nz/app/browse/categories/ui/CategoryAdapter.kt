package com.audiobookz.nz.app.browse.categories.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.databinding.ListItemCategoriesBinding

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(DiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.apply {
            category.children?.let { createOnOpenSubListListener(it, category.title) }?.let {
                bind(
                    createOnOpenAudiobookListListener(category.id.toInt(), category.title),
                    it, category)
            }

            itemView.tag = category
        }
    }

    private fun createOnOpenAudiobookListListener(id: Int, title: String): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                BrowseFragmentDirections.actionBrowseFragmentToAudiobookListFragment(id, title)
            it.findNavController().navigate(direction)
        }
    }

    private fun createOnOpenSubListListener(
        SubCategory: List<Category>,
        title: String
    ): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                BrowseFragmentDirections.actionBrowseFragmentToSubCategoryFragment(
                    SubCategory.toTypedArray(),
                    title
                )
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(
        private val binding: ListItemCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            OpenAudiobookList: View.OnClickListener,
            OpenSubCategory: View.OnClickListener,
            item: Category
        ) {
            binding.apply {
                openAudiobookList = OpenAudiobookList
                openSubCategory = OpenSubCategory
                audioCategory = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCategoriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        //To change body of created functions use File | Settings | File Templates.
    }
}

public class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }
}