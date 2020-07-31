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
        use_credit: RequestBody
    ) = getResult { service.orderCheckout(orderId, cancel_url, return_url, use_credit) }

    suspend fun orderBookList(
   // params :HashMap<String, RequestBody>
        body:RequestBody
    //a:RequestBody, b:RequestBody,  c:RequestBody, d:RequestBody, e:RequestBody, f:RequestBody
    ) = getResult { service.orderBookList(
        //a,b,c,d,e,f
//        RequestBody.create(MediaType.parse("text/plain"), "190393"),
//        RequestBody.create(MediaType.parse("text/plain"), "1"),
//        RequestBody.create(MediaType.parse("text/plain"), "191147"),
//        RequestBody.create(MediaType.parse("text/plain"), "1"),
//        RequestBody.create(MediaType.parse("text/plain"), "US"),
//        RequestBody.create(MediaType.parse("text/plain"), "")
        body
    )
    }

    fun buyCreditStatusNotification(title: String, body: String) =
        notificationService.simple(title, body)

    suspend fun getCredit() = getResult { service.getCredit() }
}