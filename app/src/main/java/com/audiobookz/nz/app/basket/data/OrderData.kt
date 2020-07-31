package com.audiobookz.nz.app.basket.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("total")
    @Expose
    val total: Float,
    @SerializedName("subtotal")
    @Expose
    val subtotal: Float,
    @SerializedName("created_at")
    @Expose
    val created_at: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("currency_code")
    @Expose
    val currency_code: String

) : Parcelable