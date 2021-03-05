package com.audiobookz.nz.app.basket.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.bookdetail.data.BookDetailRemoteDataSource
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.bookdetail.data.BookRoomDao
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLocalGetOnlyLiveData
import com.audiobookz.nz.app.more.data.MoreRemoteDataSource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BasketRepository @Inject constructor(
    private val dao: BookRoomDao,
    private val remoteSource: MoreRemoteDataSource,
    private val sharePref: SharedPreferencesService
) {
    fun loadBasket() = resultLocalGetOnlyLiveData(
        databaseQuery = { dao.loadBasket() }
    )

    fun deleteBook(id: Int) {
        dao.deleteById(id)
    }

    fun deleteBookAll() {
        dao.deleteAll()
    }

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

    fun orderBookList(
        body: List<MultipartBody.Part>, coupon: RequestBody, code: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = {
            remoteSource.orderBookList(
                body, coupon, code
            )
        })

    fun statusNotification(title: String, body: String) =
        remoteSource.statusNotification(title, body)

    fun getCredits() = resultFetchOnlyLiveData(networkCall = {
        remoteSource.getCredit()
    })

    fun saveIsSubscribed(IsSubscribed:Boolean) = sharePref.saveIsSubscribed(IsSubscribed)
}