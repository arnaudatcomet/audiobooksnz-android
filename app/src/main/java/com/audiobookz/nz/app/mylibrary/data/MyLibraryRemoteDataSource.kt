package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.FirebaseAnalyticsService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.BookEngineService
import com.audiobookz.nz.app.api.NotificationService
import com.audiobookz.nz.app.util.ConversionEvent
import io.audioengine.mobile.DownloadStatus
import javax.inject.Inject

class MyLibraryRemoteDataSource @Inject constructor(
        private val service: AudiobookService,
        private val customAnalytic: FirebaseAnalyticsService
) : BaseDataSource() {

    suspend fun getCloudBook(Page:Int,PageSize:Int) = getResult { service.getCloudBook("audiobook",Page,PageSize,"-created_at") }

    suspend fun fetchSession() = getResult { service.getSession() }

    fun addAnalytic(eventName: ConversionEvent, text: String) = customAnalytic.logEvent(event = eventName, value = text)

}