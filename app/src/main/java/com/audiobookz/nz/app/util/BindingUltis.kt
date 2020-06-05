package com.audiobookz.nz.app.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.bumptech.glide.Glide

@BindingAdapter("glideImageURL")
fun loadImage(view: ImageView, url:String?){
    Glide.with(view)
        .load(url)
        .into(view)
}
