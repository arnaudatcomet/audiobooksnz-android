package com.audiobookz.nz.app.bookdetail.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.FirebaseAnalyticsService
import com.audiobookz.nz.app.data.resultLocalSaveOnlyLiveData
import com.audiobookz.nz.app.util.ConversionEvent
import okhttp3.RequestBody
import javax.inject.Inject

class BookDetailRemoteDataSource @Inject constructor(
    private val service: AudiobookService,
    private val customAnalytic: FirebaseAnalyticsService
) : BaseDataSource() {

    suspend fun fetchBookData(bookId: Int) = getResult { service.getBookDetail(bookId) }

    suspend fun fetchLocalBookData(bookId: Int) = getResult { service.getLocalBookDetail(bookId) }

    suspend fun fetchReviewData(bookId: Int, page: Int, pageSize: Int) =
        getResult { service.getBookReview(bookId, "createdBy", page, pageSize) }

    suspend fun addWishList(bookId: RequestBody) =
        getResult { service.addAndRemoveWishList(bookId) }

    fun addAnalytic(eventName: ConversionEvent, text: String) = customAnalytic.logEvent(event = eventName, value = text)

    fun initAnalytic() = customAnalytic.initAnalytic()
}