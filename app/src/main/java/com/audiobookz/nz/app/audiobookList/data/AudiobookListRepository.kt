package com.audiobookz.nz.app.audiobookList.data


import androidx.lifecycle.distinctUntilChanged
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class AudiobookListRepository @Inject constructor(private val remoteSource: AudiobookListRemoteDataSource
) {
    fun bookList(filterID:Int,Lang :String?,Page:Int,PageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.fetchData(filterID,Lang,Page,PageSize) }
        )
}