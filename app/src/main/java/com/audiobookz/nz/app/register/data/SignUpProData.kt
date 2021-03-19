package com.audiobookz.nz.app.register.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SignUpProData(
    @SerializedName("approval_link")
    @Expose
    val approval_link: String,
    @SerializedName("subcription")
    @Expose
    val subcription: String
) : Parcelable