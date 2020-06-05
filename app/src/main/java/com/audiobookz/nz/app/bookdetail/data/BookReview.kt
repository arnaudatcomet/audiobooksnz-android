package com.audiobookz.nz.app.bookdetail.data

import android.os.Parcelable
import com.audiobookz.nz.app.login.data.UserData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class BookReview(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("audiobook_id")
    @Expose
    val audiobook_id: Int,
    @SerializedName("comment")
    @Expose
    val comment: String,
    @SerializedName("statification_rating")
    @Expose
    val statification_rating: String,
    @SerializedName("narration_rating")
    @Expose
    val narration_rating: String,
    @SerializedName("story_rating")
    @Expose
    val story_rating: String,
    @SerializedName("createdBy")
    @Expose
    val createdBy: Member
) : Parcelable
