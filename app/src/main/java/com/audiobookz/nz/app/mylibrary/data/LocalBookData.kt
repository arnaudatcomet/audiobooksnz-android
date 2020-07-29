package com.audiobookz.nz.app.mylibrary.data

import androidx.room.*
import com.audiobookz.nz.app.data.Converters
import com.google.gson.annotations.SerializedName


@Entity(tableName = "localbook")
data class LocalBookData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "bookId") val bookId: String?,
    @ColumnInfo(name = "contentId") val contentId: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "image_url") val image_url: String?,
    @ColumnInfo(name = "licenseId") val licenseId: String?,
    @ColumnInfo(name = "narrators") val narrators: String?,
    @ColumnInfo(name = "authors") val authors: String?
)
//data class LocalBookData (
//    @PrimaryKey
//    @field: SerializedName("audioengine_audiobook_id")
//    val id: String,
//    @field:SerializedName("title")
//    val title: String,
//    @field:SerializedName("cover_image")
//    val cover_image: String,
//    @Embedded
//    @field:SerializedName("audioengine_data")
//    val audioengine_data: AudioEngineData
//)
//
//data class AudioEngineData
//    (
//    @SerializedName("audiobook")
//    @Embedded
//    val copyright: AudioBookData
//)
//
//data class AudioBookData(
//    @field:SerializedName("authors")
//    val authors: List<String>,
//    @field:SerializedName("narrators")
//    val narrators: List<String>
//)

//@Entity(tableName = "AudioBookData")
//data class AudioBookData
//    (
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    @ForeignKey(entity = LocalBookData::class,
//            parentColumns = arrayOf("id"),
//        childColumns = arrayOf("bookId"))
//    val bookId:String,
//    @field:SerializedName("authors")
//    val authors: String
//)

//
//
//data class BookWithAuthor(
//    @Embedded val localBookData: LocalBookData
////    @Relation(
////        parentColumn = "id",
////        entityColumn = "bookId"
////    ) val audioBookData: List<AudioBookData>
//)
