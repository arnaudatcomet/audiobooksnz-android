package com.audiobookz.nz.app.more.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.basket.data.OrderData
import com.audiobookz.nz.app.basket.data.PaymentData
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.more.data.*
import com.audiobookz.nz.app.register.data.SignUpProData
import com.audiobookz.nz.app.util.ConversionEvent
import com.audiobookz.nz.app.util.WEB_URL
import com.stripe.android.Stripe
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MoreViewModel @Inject constructor(private val repository: MoreRepository) : ViewModel() {
    var resultRemoveWishList = MediatorLiveData<Result<WishListData>>()
    var resultGetWishList = MediatorLiveData<Result<List<WishListData>>>()
    var resultGetCardList = MediatorLiveData<Result<MutableMap<String, Any?>>>()
    var resultBuyCredits = MediatorLiveData<Result<OrderData>>()
    var resultPayment = MediatorLiveData<Result<PaymentData>>()
    var getCurrentPlanResult = MediatorLiveData<Result<List<SubscriptionsData>>>()
    var resultUpgrade = MediatorLiveData<Result<SignUpProData>>()
    var resultAddCard = MediatorLiveData<Result<CardData>>()
    var resultAddCardLocal = MediatorLiveData<Result<String>>()
    val resultLocalCardList = MediatorLiveData<Result<List<CardData>>>()
    var resultCheckSubscript = MediatorLiveData<Result<UserData>>()
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

    fun showPurchaseCreditAnalytic(orderId: Int?){
        repository.addAnalytic(ConversionEvent.purchase, "Purchase Credit OrderId $orderId")
    }

    fun showRenewSubscriptionAnalytic(){
        repository.addAnalytic(ConversionEvent.app_store_subscription_renew, "Renew Subscription")
    }

    fun orderCheckout(orderId: Int, creditUse: String, cardId: String) {
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

        var requestCard = RequestBody.create(MediaType.parse("text/plain"), cardId)
        var requestSaveCard = RequestBody.create(MediaType.parse("text/plain"), "0")
        var requestStripeToken = RequestBody.create(MediaType.parse("text/plain"), "")

        // firebase analytic
        repository.addAnalytic(ConversionEvent.begin_checkout, "Order ID $orderId")

        resultPayment.addSource(
            repository.orderCheckout(
                orderId,
                requestCancel,
                requestReturn,
                requestUseCredit,
                requestCard,
                requestSaveCard,
                requestStripeToken
            )
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

    fun upgradePro() {
        var requestCancel = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/new-site/user/subscription_agreement_cancel"
        )
        var requestSuccess = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/new-site/user/subscription_agreement_success"
        )


        resultUpgrade.addSource(
            repository.upgradePro(requestCancel, requestSuccess)
        ) { value -> resultUpgrade.value = value }
    }

    fun addPaymentCard(
        cardToken: String,
        number: String,
        cvc: String,
        month: String,
        year: String
    ) {
        var stripeToken = RequestBody.create(
            MediaType.parse("text/plain"), cardToken
        )
        resultAddCard.addSource(
            repository.addPaymentCard(stripeToken, number, cvc, month, year)
        ) { value -> resultAddCard.value = value }
    }

    fun getCardList(localCard: List<CardData>?) {
        resultGetCardList.addSource(
            repository.getCardList()
        ) { value ->
            if (value.data != null) {
                val map: MutableMap<String, Any?> = mutableMapOf()
                map["cloud"] = value.data
                map["local"] = localCard

                resultGetCardList.value = Result.success(map)
            }
        }
    }

    fun removeCardList(cardId: String) {
        var requestCall = repository.removeCardList(cardId)
        requestCall.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //do nothing
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    //To Do: delete card in local
                    getLocalCard()
                }
            }
        })
    }

    fun setDefaultCard(cardId: String) {
        var requestCall = repository.setDefaltCard(cardId)
        requestCall.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //do nothing
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    getLocalCard()
                }
            }
        })
    }

    fun getLocalCard() {
        resultLocalCardList.addSource(repository.getLocalCardList()) { value ->
            resultLocalCardList.value = value
        }
    }

    fun addCardLocal(
        cardId: String,
        number: String,
        cvc: String,
        month: String,
        year: String
    ) {
        resultAddCardLocal.addSource(
            repository.saveCardDetailLocal(
                cardId,
                number,
                cvc,
                month,
                year
            )
        ) { value ->
            resultAddCardLocal.value = value
        }
    }

    fun checkSubscript() {
        resultCheckSubscript.addSource(repository.getProfile()) { value ->
            resultCheckSubscript.value = value
        }
    }

    fun statusNotification(title: String, body: String) =
        repository.statusNotification(title, body)

}
