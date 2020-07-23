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
    val id: Int
) : Parcelable