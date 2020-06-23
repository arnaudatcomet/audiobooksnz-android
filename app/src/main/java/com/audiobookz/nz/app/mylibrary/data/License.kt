package com.audiobookz.nz.app.mylibrary.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class License(
    @SerializedName("modified_date")
    @Expose
    val modified_date: String,
    @SerializedName("content_id")
    @Expose
    val content_id: String,
    @SerializedName("account_id")
    @Expose
    val account_id: String,
    @SerializedName("business_model")
    @Expose
    val business_model: String,
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("product_id")
    @Expose
    val product_id: String
    ) : Parcelable