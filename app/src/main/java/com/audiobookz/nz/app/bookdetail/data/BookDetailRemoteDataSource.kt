package com.audiobookz.nz.app.bookdetail.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

class BookDetailRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun fetchBookData(bookId: Int) = getResult { service.getBookDetail(bookId) }

    suspend fun fetchLocalBookData(bookId: Int) = getResult { service.getLocalBookDetail(bookId) }


    suspend fun fetchReviewData(bookId: Int,page:Int,pageSize:Int) = getResult { service.getBookReview(bookId,"createdBy",page,pageSize) }
}