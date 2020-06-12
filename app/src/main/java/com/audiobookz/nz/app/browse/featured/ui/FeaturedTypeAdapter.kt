package com.audiobookz.nz.app.browse.featured.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.App
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.databinding.ListItemFeaturedBinding
import com.audiobookz.nz.app.databinding.ListItemFeaturedBookBinding
import com.audiobookz.nz.app.util.FEATURED_BOOK_SHOW
import kotlinx.android.synthetic.main.list_item_featured.view.*


class FeaturedTypeAdapter(val resultData: Map<String, List<Featured>>) :
    RecyclerView.Adapter<FeaturedTypeAdapter.ViewHolder>() {

    class ViewHolder(view: ListItemFeaturedBinding) :
        RecyclerView.ViewHolder(view.root) {
        val rvMain = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFeaturedBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return resultData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookAdapter = BookAdapter()
        var currentFeaturedKey = resultData.keys.toTypedArray()[position]
        var currentFeaturedList = resultData[currentFeaturedKey]

        holder.rvMain.typeName.text = resultData.keys.toTypedArray()[position]
        bookAdapter.submitList(currentFeaturedList?.take(FEATURED_BOOK_SHOW))

        holder.rvMain.openBookList = openBookList(currentFeaturedList!!.toTypedArray(),currentFeaturedKey)

        holder.itemView.nestRecyclerView.apply {
            adapter = bookAdapter
        }
    }

    private fun openBookList(listFeature:Array<Featured>, title:String): View.OnClickListener {
        return View.OnClickListener {
            val direction = BrowseFragmentDirections.actionBrowseFragmentToAudiobookListFragment(
                id = 0,
                listItem = listFeature,
                titleList = title
            )
            it.findNavController().navigate(direction)
        }
    }
}

