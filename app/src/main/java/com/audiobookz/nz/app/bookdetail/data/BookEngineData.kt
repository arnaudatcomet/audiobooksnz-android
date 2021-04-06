package com.audiobookz.nz.app.bookdetail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class BookEngineData(
    @SerializedName("audiobook")
    @Expose
    val BookDetail: BookDetail?,
    @SerializedName("active_products")
    @Expose
    val active_products: ActiveProductsModel?
) : Parcelable
