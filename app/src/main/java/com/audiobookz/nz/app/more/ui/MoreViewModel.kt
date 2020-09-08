package com.audiobookz.nz.app.more.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.basket.data.OrderData
import com.audiobookz.nz.app.basket.data.PaymentData
import com.audiobookz.nz.app.more.data.MoreRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.more.data.SubscriptionsData
import com.audiobookz.nz.app.more.data.WishListData
import com.audiobookz.nz.app.util.WEB_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoreViewModel @Inject constructor(private val repository: MoreRepository) : ViewModel() {
    var resultRemoveWishList = MediatorLiveData<Result<WishListData>>()
    var resultGetWishList = MediatorLiveData<Result<List<WishListData>>>()
    var resultBuyCredits = MediatorLiveData<Result<OrderData>>()
    var resultPayment = MediatorLiveData<Result<PaymentData>>()
    var getCurrentPlanResult = MediatorLiveData<Result<List<SubscriptionsData>>>()
    var page = 1
    var pageSize = 30

    fun getWishList() {
        resultGetWishList.addSource(
            repository.getWishList(page, pageSize)
        ) { value -> resultGetWishList.value = value }
    }

    fun removeWishList(BookId: Int) {
        var requestBookId = RequestBody.create(MediaType.parse("text/plain"), BookId.toString())
        resultRemoveWishList.addSource(
            repository.removeWishList(requestBookId)
        ) { value -> resultRemoveWishList.value = value }
    }

    fun buyCredits(credits: String, code: String) {
        var requestCredits = RequestBody.create(MediaType.parse("text/plain"), credits)
        //product_id = "0" it mean Credit??
        var requestProduct = RequestBody.create(MediaType.parse("text/plain"), "0")
        var requestType = RequestBody.create(MediaType.parse("text/plain"), "3")
        var requestCountry = RequestBody.create(MediaType.parse("text/plain"), code)

        resultBuyCredits.addSource(
            repository.buyCredits(requestCredits, requestProduct, requestType, requestCountry)
        ) { value -> resultBuyCredits.value = value }
    }

    fun orderCheckout(orderId: Int, creditUse: String) {
        var requestCancel = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/cart/paypal_fail"
        )
        var requestReturn = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/cart/paypal_success"
        )
        //if in confirm order checkbox use credit is true useCredit = 1, default 0
        var requestUseCredit = RequestBody.create(MediaType.parse("text/plain"), creditUse)

        resultPayment.addSource(
            repository.orderCheckout(orderId, requestCancel, requestReturn, requestUseCredit)
        ) { value -> resultPayment.value = value }
    }

    fun getCurrentPlan() {
        getCurrentPlanResult.addSource(
            (repository.getCurrentPlan(
                1,
                10
            ))
        ) { value ->
            getCurrentPlanResult.value = value
        }
    }

    fun deleteSubscriptions(subscriptionId: Int) {
        var requestCall = repository.deleteSubscriptions(subscriptionId)
        requestCall.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //do nothing
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    getCurrentPlan()
                }
            }
        })
    }
}
