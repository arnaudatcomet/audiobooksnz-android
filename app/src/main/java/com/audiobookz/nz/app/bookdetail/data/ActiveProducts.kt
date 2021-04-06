package com.audiobookz.nz.app.bookdetail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ActiveProductsModel (
    @SerializedName("credit_subscription")
    @Expose
    val credit_subscription: List<CreditSubscriptionModel>?,
    @SerializedName("retail")
    @Expose
    val retail: List<RetailModel>?
): Parcelable

@Parcelize
class CreditSubscriptionModel (
    @SerializedName("territories")
    @Expose
    val territories: List<String>?
): Parcelable

@Parcelize
class RetailModel (
    @SerializedName("territories")
    @Expose
    val territories: List<String>?
): Parcelable

