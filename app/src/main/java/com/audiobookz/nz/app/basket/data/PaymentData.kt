package com.audiobookz.nz.app.basket.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentData(
    @SerializedName("approval_url")
    @Expose
    val approval_url: String,
    @SerializedName("state")
    @Expose
    val state: String,
    @SerializedName("msg")
    @Expose
    val msg: String
) : Parcelable