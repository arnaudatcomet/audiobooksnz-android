package com.audiobookz.nz.app.bookdetail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class Member(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("first_name")
    @Expose
    val first_name: String,
    @SerializedName("last_name")
    @Expose
    val last_name: String,
    @SerializedName("image_file")
    @Expose
    val image_file: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("status_id")
    @Expose
    val status_id: String,
    @SerializedName("image_url")
    @Expose
    val image_url: String,
    @SerializedName("isSubscribed")
    @Expose
    val isSubscribed: Boolean,
    @SerializedName("isGiftAvailable")
    @Expose
    val isGiftAvailable: Boolean
) : Parcelable
