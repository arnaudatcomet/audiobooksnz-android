package com.audiobookz.nz.app.mylibrary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chapterData")
data class ChapterData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "contentId") val contentId: Int?,
    @ColumnInfo(name = "image_url") val image_url: String?,
    @ColumnInfo(name = "mp3_url") val mp3_url: String?
)