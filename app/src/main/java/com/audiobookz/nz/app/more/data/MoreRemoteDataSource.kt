package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.NotificationService
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRemoteDataSource @Inject constructor(
    private val service: AudiobookService,
    private val notificationService: NotificationService
) :
    BaseDataSource() {

    suspend fun getWishList(page: Int, pageSize: Int) =
        getResult { service.getWishList(page, pageSize) }

    suspend fun removeWishList(bookId: RequestBody) =
        getResult { service.addAndRemoveWishList(bookId) }

    suspend fun buyCredits(
        quantity: RequestBody,
        product_id: RequestBody,
        type_id: RequestBody,
        country_code: RequestBody
    ) = getResult { service.buyCredits(quantity, product_id, type_id, country_code) }

    suspend fun orderCheckout(
        orderId: Int,
        cancel_url: RequestBody,
        return_url: RequestBody,
        use_credit: RequestBody,
        card: RequestBody,
        save_card: RequestBody,
        stripe_token: RequestBody

    ) = getResult { service.orderCheckout(orderId, cancel_url, return_url, use_credit,card,save_card,stripe_token) }

    suspend fun orderBookList(
        body: List<MultipartBody.Part>, coupon: RequestBody, code: RequestBody
    ) = getResult {
        service.orderBookList(
            body, coupon, code
        )
    }

    fun statusNotification(title: String, body: String) =
        notificationService.simple(title, body)

    suspend fun getCredit() = getResult { service.getCredit() }

    suspend fun getCurrentPlan(Page: Int, PageSize: Int) =
        getResult { service.getCurrentPlan("plan", Page, PageSize, "-created_at") }

    fun deleteSubscriptions(subscriptionId: Int) = service.deleteSubscriptions(subscriptionId)

    suspend fun upgradePro(
        cancel_url: RequestBody,
        success_url: RequestBody
    ) = getResult { service.signUpPro(cancel_url, success_url) }

    suspend fun addPaymentCard(
        stripe_token: RequestBody
    ) = getResult { service.addPaymentCard(stripe_token) }

    suspend fun getCardList() =
        getResult { service.getListCard() }

    fun removeCardList(cardId: String) = service.deleteCard(cardId)

    fun setDefaultCard(cardId: String) = service.setCardDefault(cardId)

    suspend fun getProfile() = getResult { service.getProfile() }
}