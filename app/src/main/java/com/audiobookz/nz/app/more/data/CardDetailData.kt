package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import com.audiobookz.nz.app.bookdetail.data.BookEngineData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardDetailData(
    @SerializedName("brand")
    @Expose
    val brand: String,
    @SerializedName("last4")
    @Expose
    val last4: String,
    @SerializedName("exp_month")
    @Expose
    val exp_month: String,
    @SerializedName("exp_year")
    @Expose
    val exp_year: String,
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String?
) : Parcelable