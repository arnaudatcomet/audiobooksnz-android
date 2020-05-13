package com.audiobookz.nz.app.browse.categories.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field: SerializedName("sort_order")
    val sortOrder: Int,
    @field: SerializedName("slug")
    val slug: String,
    @field: SerializedName("title")
    val title: String,
    @field: SerializedName("description")
    val description: String
) {
    override fun toString() = title
}
