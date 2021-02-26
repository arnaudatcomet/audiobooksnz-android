package com.audiobookz.nz.app.login.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "userdata")
data class UserData(
    @PrimaryKey
    @field: SerializedName("id")
    val id: Int?,
    @field:SerializedName("access_token")
    val access_token: String?,
    @field: SerializedName("access_token_expire")
    val access_token_expire: Int,
    @field: SerializedName("first_name")
    val first_name: String?,
    @field: SerializedName("last_name")
    val last_name: String?,
    @field: SerializedName("full_name")
    val full_name: String?,
    @field: SerializedName("image_file")
    val image_file: String?,
    @field: SerializedName("email")
    val email: String?,
    @field: SerializedName("status_id")
    val status_id: Int?,
    @field: SerializedName("stripe_fingerprint")
    val stripe_fingerprint: String?,
    @field: SerializedName("stripe_customer_id")
    val stripe_customer_id: String?,
    @field: SerializedName("image_url")
    val image_url: String?,
    @field: SerializedName("isSubscribed")
    val isSubscribed: Boolean?,
    @field: SerializedName("isGiftAvailable")
    val isGiftAvailable: Boolean?,
    @field: SerializedName("credit_count")
    val credit_count: Int?=null
    )