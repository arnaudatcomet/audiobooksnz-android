package com.audiobookz.nz.app.bookdetail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class BookDetail(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("slug")
    @Expose
    val slug: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("business_model")
    @Expose
    val business_model: Int,
    @SerializedName("cover_image")
    @Expose
    val cover_image: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("price_currency")
    @Expose
    val price_currency: String,
    @SerializedName("credit_price")
    @Expose
    val credit_price: Int,
    @SerializedName("avg_rating")
    @Expose
    val avg_rating: String,
    @SerializedName("cover_image_url")
    @Expose
    val cover_image_url: String,
    @SerializedName("is_bought")
    @Expose
    val is_bought: Boolean,
    @SerializedName("in_wishlist")
    @Expose
    val in_wishlist: Boolean,
    @SerializedName("review_count")
    @Expose
    val review_count: String,
    @SerializedName("publisher")
    @Expose
    val publisher: String,
    @SerializedName("chapterized")
    @Expose
    val chapterized: Boolean,
    @SerializedName("cover_url")
    @Expose
    val cover_url: String,
    @SerializedName("authors")
    @Expose
    val authors: List<String>



    ) : Parcelable
