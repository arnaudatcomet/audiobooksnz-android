package com.audiobookz.nz.app.player.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.audiobookz.nz.app.mylibrary.data.LicenseData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SynPositionData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("audiobook_id")
    @Expose
    val audiobook_id: Int,
    @SerializedName("created_by")
    @Expose
    val created_by: Int,
    @SerializedName("created_at")
    @Expose
    val created_at: Long,
    @SerializedName("playback_position")
    @Expose
    val playback_position: PlaybackData?

) : Parcelable