package com.audiobookz.nz.app.audiobookList.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject
class AudiobookListRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun fetchData(filter: Int) = getResult { service.getAudiobooksList(filter,2,10,"English") }
}
