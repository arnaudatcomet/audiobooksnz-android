package com.audiobookz.nz.app.mylibrary.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class LicenseData(
    @SerializedName("licenses")
    @Expose
    val licenses: List<License>
) : Parcelable