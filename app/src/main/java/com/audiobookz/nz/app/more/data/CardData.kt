package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "carddata")
data class CardData(
    @PrimaryKey
    @field: SerializedName("card_id")
    val card_id: String,
    @field: SerializedName("number")
    var number: String,
    @field: SerializedName("cvc")
    var cvc: String,
    @field: SerializedName("exp_month")
    var exp_month: String,
    @field: SerializedName("exp_year")
    var exp_year: String,
    @field:SerializedName("stripe_fingerprint")
    val stripe_fingerprint: String = "",
    @field:SerializedName("isSubscribed")
    val isSubscribed: Boolean = false

//    @SerializedName("access_token")
//    @Expose
//    val access_token: String,
//    @SerializedName("access_token_expire")
//    @Expose
//    val access_token_expire: Long,
//    @SerializedName("first_name")
//    @Expose
//    val first_name: String,
//    @SerializedName("last_name")
//    @Expose
//    val last_name: String,
//    @SerializedName("image_file")
//    @Expose
//    val image_file: String,
//    @SerializedName("role_id")
//    @Expose
//    val role_id: String,
//    @SerializedName("email")
//    @Expose
//    val email: String,
//    @SerializedName("register_via")
//    @Expose
//    val register_via: String,
//    @SerializedName("status_id")
//    @Expose
//    val status_id: Int,
//    @SerializedName("stripe_customer_id")
//    @Expose
//    val stripe_customer_id: String,
//    @SerializedName("extra_fields")
//    @Expose
//    val extra_fields: String,
//    @SerializedName("image_url")
//    @Expose
//    val image_url: String,
//    @SerializedName("isGiftAvailable")
//    @Expose
//    val isGiftAvailable: Boolean,
//    @SerializedName("isClubMember")
//    @Expose
//    val isClubMember: Boolean
)