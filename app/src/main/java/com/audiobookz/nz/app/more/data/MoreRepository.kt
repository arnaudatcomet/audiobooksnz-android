package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRepository @Inject constructor(
    private val remoteSource: MoreRemoteDataSource,
    private val sharePref: SharedPreferencesService
) {

    fun getWishList(page: Int, pageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getWishList(page, pageSize) })

    fun removeWishList(bookId: RequestBody) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.removeWishList(bookId) })

    fun buyCredits(
        quantity: RequestBody,
        product_id: RequestBody,
        type_id: RequestBody,
        country_code: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.buyCredits(quantity, product_id, type_id, country_code) })

    fun orderCheckout(
        orderId: Int,
        cancel_url: RequestBody,
        return_url: RequestBody,
        use_credit: RequestBody,
        card: RequestBody,
        save_card: RequestBody,
        stripe_token: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.orderCheckout(orderId, cancel_url, return_url, use_credit,card,save_card,stripe_token) })

    fun getCurrentPlan(Page: Int, PageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCurrentPlan(Page, PageSize) }
    )

    fun deleteSubscriptions(subscriptionId: Int) = remoteSource.deleteSubscriptions(subscriptionId)

    fun upgradePro(
        cancel_url: RequestBody,
        success_url: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = {
            remoteSource.upgradePro(cancel_url, success_url)
        })

    fun addPaymentCard(rawToken:String,
        stripe_token: RequestBody
    ) = resultSimpleLiveData(
        networkCall = {
            remoteSource.addPaymentCard(stripe_token)
        }, saveCallResult = {
            sharePref.saveIsSubscribed(it.isSubscribed)
            if (it.stripe_fingerprint.isNullOrEmpty()){
                sharePref.saveCardPayment(false)
            }else{
                sharePref.saveCardPayment(true)
            }
        },
        onCallSuccess = {})

    fun getIsSubscribed() = sharePref.getIsSubscribed()

    fun getHasCard() = sharePref.getHasCard()

    fun getCardList() = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCardList() })

    fun removeCardList(cardId: String) = remoteSource.removeCardList(cardId)

    fun setDefaltCard(cardId: String) = remoteSource.setDefaultCard(cardId)
}