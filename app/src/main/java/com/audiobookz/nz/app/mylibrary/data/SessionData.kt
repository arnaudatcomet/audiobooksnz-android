package com.audiobookz.nz.app.mylibrary.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "session")
data class SessionData (
    @PrimaryKey
    @field: SerializedName("key")
    val key: String,
    @field:SerializedName("account_id")
    val account_id: String
)