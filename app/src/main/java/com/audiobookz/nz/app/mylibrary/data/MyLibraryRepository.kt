package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import javax.inject.Inject

class MyLibraryRepository @Inject constructor(private val remoteSource: MyLibraryRemoteDataSource
) {
    fun getCloudBook(Page:Int,PageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCloudBook(Page,PageSize) }
    )
}