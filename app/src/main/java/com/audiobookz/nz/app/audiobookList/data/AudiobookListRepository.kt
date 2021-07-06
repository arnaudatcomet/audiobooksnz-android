package com.audiobookz.nz.app.audiobookList.data


import androidx.lifecycle.distinctUntilChanged
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.util.ConversionEvent
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
    fun searchList(filter:String,Page:Int,PageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.searchData(filter,Page,PageSize) }
        )

    fun addAnalytic(eventName: ConversionEvent, text: String) =
            remoteSource.addAnalytic(eventName, text)

}