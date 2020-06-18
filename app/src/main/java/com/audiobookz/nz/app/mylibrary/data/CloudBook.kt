package com.audiobookz.nz.app.mylibrary.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CloudBook(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("audiobook")
    @Expose
    val audiobook: Audiobook?

) : Parcelable