package com.audiobookz.nz.app.browse.categories.ui
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.databinding.ListItemSubCategoriesBinding

class SubCategoryAdapter : ListAdapter<Category,SubCategoryAdapter.ViewHolder>(DiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.ViewHolder {
        return ViewHolder(
            ListItemSubCategoriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(
        private val binding: ListItemSubCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(OpenAudiobookList: View.OnClickListener,item: Category) {
            binding.apply {
                audioCategory = item
                clickListener = OpenAudiobookList
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.apply {
            bind(createOnOpenAudiobookListListener(category.id, category.title),category)
            itemView.tag = category
        }
    }
    private fun createOnOpenAudiobookListListener(id: String, title:String): View.OnClickListener {
        return View.OnClickListener {
            val direction = SubCategoryFragmentDirections.actionSubCategoryFragmentToAudiobookListFragment(id.toInt(), title)
            it.findNavController().navigate(direction)
        }
    }

}