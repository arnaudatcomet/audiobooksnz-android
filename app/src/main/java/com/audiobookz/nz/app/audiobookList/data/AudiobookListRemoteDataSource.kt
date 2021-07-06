package com.audiobookz.nz.app.audiobookList.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.FirebaseAnalyticsService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.util.ConversionEvent
import javax.inject.Inject
class AudiobookListRemoteDataSource @Inject constructor(
        private val service: AudiobookService,
        private val customAnalytic: FirebaseAnalyticsService
) : BaseDataSource() {

    suspend fun fetchData(filter: Int,Lang:String?,Page:Int,PageSize:Int) = getResult { service.getAudiobooksList(filter,Page,PageSize,Lang) }

    suspend fun searchData(filter: String,Page:Int,PageSize:Int) = getResult { service.getSearchList(filter,Page,PageSize) }

    fun addAnalytic(eventName: ConversionEvent, text: String) = customAnalytic.logEvent(event = eventName, value = text)

}
