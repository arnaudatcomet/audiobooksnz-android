package com.audiobookz.nz.app.bookdetail.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookroom")
data class BookRoom(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "image_url") val image_url: String?,
    @ColumnInfo(name = "credit_price") val credit_price: Int?
)