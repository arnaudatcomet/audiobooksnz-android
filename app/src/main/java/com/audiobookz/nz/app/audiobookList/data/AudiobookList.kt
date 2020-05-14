package com.audiobookz.nz.app.audiobookList.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "audiobookList")
data class AudiobookList(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field: SerializedName("price")
    val price: String,
    @field: SerializedName("price_currency")
    val price_currency: String,
    @field: SerializedName("title")
    val title: String,
    @field: SerializedName("description")
    val description: String,
    var CategoryID:Int? = null

) {
    override fun toString() = title
}
