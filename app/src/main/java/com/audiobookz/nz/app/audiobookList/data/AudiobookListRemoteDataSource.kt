package com.audiobookz.nz.app.audiobookList.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject
class AudiobookListRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun fetchData(filter: Int,Lang:String?,Page:Int,PageSize:Int) = getResult { service.getAudiobooksList(filter,Page,PageSize,Lang) }
}
