package com.audiobookz.nz.app.browse.categories.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Category(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("sort_order")
    @Expose
    val sortOrder: Int,
    @SerializedName("slug")
    @Expose
    val slug: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("parent_id")
    @Expose
    val parent_id: String,
    @SerializedName("sub_category_count")
    @Expose
    val sub_category_count: String,
    @SerializedName("children")
    @Expose
    val children: List<Category>?=null
) : Parcelable
