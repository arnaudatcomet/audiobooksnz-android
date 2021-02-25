package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardData(
    @SerializedName("card_id")
    @Expose
    val card_id: String,
    @SerializedName("access_token")
    @Expose
    val access_token: String,
    @SerializedName("access_token_expire")
    @Expose
    val access_token_expire: Long,
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
    @SerializedName("role_id")
    @Expose
    val role_id: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("register_via")
    @Expose
    val register_via: String,
    @SerializedName("status_id")
    @Expose
    val status_id: Int,
    @SerializedName("stripe_fingerprint")
    @Expose
    val stripe_fingerprint: String,
    @SerializedName("stripe_customer_id")
    @Expose
    val stripe_customer_id: String,
//    @SerializedName("extra_fields")
//    @Expose
//    val extra_fields: String,
    @SerializedName("image_url")
    @Expose
    val image_url: String,
    @SerializedName("isSubscribed")
    @Expose
    val isSubscribed: Boolean,
    @SerializedName("isGiftAvailable")
    @Expose
    val isGiftAvailable: Boolean,
    @SerializedName("isClubMember")
    @Expose
    val isClubMember: Boolean

) : Parcelable