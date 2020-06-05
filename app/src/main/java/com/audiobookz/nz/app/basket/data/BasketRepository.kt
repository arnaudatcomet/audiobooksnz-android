package com.audiobookz.nz.app.basket.data

import com.audiobookz.nz.app.bookdetail.data.BookDetailRemoteDataSource
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.bookdetail.data.BookRoomDao
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLocalGetOnlyLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BasketRepository @Inject constructor(private val dao: BookRoomDao
) {
    fun loadBasket() = resultLocalGetOnlyLiveData(
        databaseQuery = {dao.loadBasket()}
    )
}