package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRemoteDataSource@Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun getWishList(page:Int, pageSize: Int) = getResult { service.getWishList(page,pageSize) }

    suspend fun removeWishList(bookId: RequestBody) = getResult { service.addAndRemoveWishList(bookId) }
}