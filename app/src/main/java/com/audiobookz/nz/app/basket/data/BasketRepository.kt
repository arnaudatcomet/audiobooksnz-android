package com.audiobookz.nz.app.basket.data

import com.audiobookz.nz.app.bookdetail.data.BookDetailRemoteDataSource
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.bookdetail.data.BookRoomDao
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLocalGetOnlyLiveData
import com.audiobookz.nz.app.more.data.MoreRemoteDataSource
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BasketRepository @Inject constructor(private val dao: BookRoomDao,private val remoteSource: MoreRemoteDataSource
) {
    fun loadBasket() = resultLocalGetOnlyLiveData(
        databaseQuery = {dao.loadBasket()}
    )
    fun deleteBook(id:Int){
        dao.deleteById(id)
    }

    fun orderCheckout(
        orderId: Int,
        cancel_url: RequestBody,
        return_url: RequestBody,
        use_credit: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.orderCheckout(orderId, cancel_url, return_url, use_credit) })

    fun orderBookList(
        orderItem: ArrayList<BookRoom>,
        country_code: String,
        coupon_code: String?
    ) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.orderBookList(orderItem, country_code, coupon_code) })
}