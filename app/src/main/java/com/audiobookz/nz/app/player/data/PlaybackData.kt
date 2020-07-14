package com.audiobookz.nz.app.player.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class PlaybackData(
    @SerializedName("chapter")
    @Expose
    val chapter: Int,
    @SerializedName("part_number")
    @Expose
    val part_number: Int,
    @SerializedName("position_in_millisecond")
    @Expose
    val position_in_millisecond: Float,
    @SerializedName("subtitle")
    @Expose
    val subtitle: String?

) : Parcelable