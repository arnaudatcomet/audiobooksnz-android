package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardListData(
    @SerializedName("card")
    @Expose
    val card: List<CardDetailData>,
    @SerializedName("default")
    @Expose
    val default: String
) : Parcelable