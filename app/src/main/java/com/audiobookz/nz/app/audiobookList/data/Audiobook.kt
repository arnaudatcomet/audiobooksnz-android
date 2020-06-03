package com.audiobookz.nz.app.audiobookList.data


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
class Audiobook(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("cover_image")
    @Expose
    val cover_image: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("price")
    @Expose
    val price:String,
    @SerializedName("price_currency")
    @Expose
    val price_currency:String,
    @SerializedName("credit_price")
    @Expose
    val credit_price:Int,
    @SerializedName("avg_rating")
    @Expose
    val avg_rating:String,
    @SerializedName("authors")
    @Expose
    val authors:List<String>
) : Parcelable
