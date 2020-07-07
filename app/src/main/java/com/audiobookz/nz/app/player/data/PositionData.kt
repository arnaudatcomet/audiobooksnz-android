package com.audiobookz.nz.app.player.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.audiobookz.nz.app.mylibrary.data.LicenseData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class PositionData(
    @PrimaryKey
    @field: SerializedName("id")
    val id: Int,
    @field: SerializedName("addSuccess")
    val addSuccess: String
)