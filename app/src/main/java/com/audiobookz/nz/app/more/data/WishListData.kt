package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class WishListData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("audiobook_id")
    @Expose
    val audiobook_id: Int,
    @SerializedName("created_at")
    @Expose
    val created_at: Int,
    @SerializedName("audiobook")
    @Expose
    val audiobook: Audiobook?

) : Parcelable