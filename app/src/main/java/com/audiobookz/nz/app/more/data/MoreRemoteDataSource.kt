package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRemoteDataSource @Inject constructor(private val service: AudiobookService) :
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
        use_credit: RequestBody
    ) = getResult { service.orderCheckout(orderId, cancel_url, return_url, use_credit) }

    suspend fun orderBookList(
        orderItem: ArrayList<BookRoom>,
        country_code: String,
        coupon_code: String?
    ) = getResult { service.orderBookList(orderItem, country_code, coupon_code) }

}