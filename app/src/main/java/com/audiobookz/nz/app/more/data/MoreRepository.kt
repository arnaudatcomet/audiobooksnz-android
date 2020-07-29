package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRepository @Inject constructor(private val remoteSource: MoreRemoteDataSource) {

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
        use_credit: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.orderCheckout(orderId, cancel_url, return_url, use_credit) })
}