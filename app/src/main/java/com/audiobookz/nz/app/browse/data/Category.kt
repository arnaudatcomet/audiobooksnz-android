package com.audiobookz.nz.app.browse.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field: SerializedName(value = "sort_order")
    val sortOrder: Int,
    @field: SerializedName(value = "slug")
    val slug: String,
    @field: SerializedName(value = "title")
    val title: String,
    @field: SerializedName(value = "Description")
    val description: String
) {
    override fun toString() = title
}
