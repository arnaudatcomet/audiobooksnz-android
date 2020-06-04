package com.audiobookz.nz.app.bookdetail.data

import androidx.lifecycle.distinctUntilChanged
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRemoteDataSource
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultRoomSaveOnlyLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookDetailRepository @Inject constructor(private val remoteSource: BookDetailRemoteDataSource,private val dao: BookRoomDao
) {
    fun bookDetailData(bookId:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.fetchBookData(bookId) }
    )
    fun bookReviewData(bookId:Int,page:Int,pageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.fetchReviewData(bookId,page,pageSize) }
    )
    fun addCard(id:Int?,title:String?,image:String?,price:String?,credit_price:Int?)  = resultRoomSaveOnlyLiveData(
        saveCallResult = { dao.insertCart(BookRoom(id,title,image,price,credit_price))}
    )
    fun countCart()= dao.getCount()

}