package com.audiobookz.nz.app.player.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class BookmarksData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("user_audiobook_id")
    @Expose
    val user_audiobook_id: Int,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("created_at")
    @Expose
    var created_at: Long,
    @SerializedName("playback_position")
    @Expose
    val playback_position: PlaybackData?

) : Parcelable