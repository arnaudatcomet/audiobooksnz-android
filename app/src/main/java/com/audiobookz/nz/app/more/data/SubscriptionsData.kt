package com.audiobookz.nz.app.more.data

import android.os.Parcelable
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SubscriptionsData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("paypal_subscription_id")
    @Expose
    val paypal_subscription_id: String,
    @SerializedName("start_time")
    @Expose
    val start_time: String,
    @SerializedName("end_time")
    @Expose
    val end_time: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("plan")
    @Expose
    val plan: PlanData?

) : Parcelable