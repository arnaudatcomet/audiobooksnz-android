package com.audiobookz.nz.app.more.data

import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import okhttp3.RequestBody
import javax.inject.Inject

class MoreRepository @Inject constructor(private val remoteSource: MoreRemoteDataSource) {

    fun getWishList(page: Int, pageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getWishList(page, pageSize) })

    fun removeWishList(bookId: RequestBody) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.removeWishList(bookId) })
}