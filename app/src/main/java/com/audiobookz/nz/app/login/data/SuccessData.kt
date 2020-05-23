package com.audiobookz.nz.app.login.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "successData")
data class SuccessData(
    @PrimaryKey
    @field: SerializedName("success")
    val success: String

)