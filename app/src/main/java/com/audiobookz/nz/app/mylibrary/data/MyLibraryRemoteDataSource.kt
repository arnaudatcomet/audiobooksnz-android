package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.BookEngineService
import com.audiobookz.nz.app.api.NotificationService
import io.audioengine.mobile.DownloadStatus
import javax.inject.Inject

class MyLibraryRemoteDataSource @Inject constructor(private val service: AudiobookService, private val bookEngineService: BookEngineService, private val notificationService: NotificationService) : BaseDataSource() {

    suspend fun getCloudBook(Page:Int,PageSize:Int) = getResult { service.getCloudBook("audiobook",Page,PageSize) }
    fun download(contentId: String, licenseId: String) = bookEngineService.download(contentId,licenseId)
    fun delete(contentId: String, licenseId: String) = bookEngineService.delete(contentId, licenseId)
    fun cancelDownload(contentId: String, licenseId: String) = bookEngineService.cancelDownload(contentId, licenseId)
    fun notifySimpleNotification(title:String, body:String)=notificationService.simple(title,body)
    fun getLocalBook(status: DownloadStatus)= bookEngineService.getLocalBook(status)
    fun getContentStatus(contentId :String) = bookEngineService.contentStatus(contentId)
    suspend fun fetchSession() = getResult { service.getSession() }

}