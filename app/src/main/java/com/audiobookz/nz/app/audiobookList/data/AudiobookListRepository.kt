package com.audiobookz.nz.app.audiobookList.data


import androidx.lifecycle.distinctUntilChanged
import com.audiobookz.nz.app.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class AudiobookListRepository @Inject constructor(private val dao: AudiobookListDao,
                                                  private val remoteSource: AudiobookListRemoteDataSource
) {
    fun categoryDetail(filterID:Int) = resultLiveData(
        databaseQuery = { dao.getAudiobookList() },
        networkCall = { remoteSource.fetchData(filterID) },
        saveCallResult = { dao.insertAudiobookList(it) },
        nukeAudiobookList = {dao.nukeAudiobookList()}
        ).distinctUntilChanged()
}