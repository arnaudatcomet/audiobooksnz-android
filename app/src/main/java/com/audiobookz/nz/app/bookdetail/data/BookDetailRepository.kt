package com.audiobookz.nz.app.bookdetail.data

import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLocalSaveOnlyLiveData
import com.audiobookz.nz.app.util.ConversionEvent
import okhttp3.RequestBody
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
    fun addCard(id: Int?, title:String?, image:String?, price:String?, credit_price:Int?)  = resultLocalSaveOnlyLiveData(
        saveCallResult = { dao.insertCart(BookRoom(id,title,image,price,credit_price))}
    )
    fun countCart()= dao.getCount()

    fun addWishList(bookId: RequestBody) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.addWishList(bookId) })

    fun addAnalytic(eventName: ConversionEvent, text: String) =
        remoteSource.addAnalytic(eventName, text)

    fun initAnalytic() = remoteSource.initAnalytic()
}