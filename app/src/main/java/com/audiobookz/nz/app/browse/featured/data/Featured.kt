package com.audiobookz.nz.app.browse.featured.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Featured(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("sort_order")
    @Expose
    val sortOrder: Int,
    @SerializedName("type_id")
    @Expose
    val type_id: Int,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("audiobook")
    @Expose
    val audiobook: List<Audiobook>
) : Parcelable
