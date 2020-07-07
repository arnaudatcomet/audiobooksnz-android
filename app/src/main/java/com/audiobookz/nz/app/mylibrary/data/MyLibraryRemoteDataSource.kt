package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.BookEngineService
import com.audiobookz.nz.app.api.NotificationService
import io.audioengine.mobile.DownloadStatus
import javax.inject.Inject

class MyLibraryRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun getCloudBook(Page:Int,PageSize:Int) = getResult { service.getCloudBook("audiobook",Page,PageSize) }

    suspend fun fetchSession() = getResult { service.getSession() }

    suspend fun postChapterPosition(bookID:Int, chapter:Int, position:Long, part:Int) = getResult {service.postChapterPosition(bookID, chapter, position, part)}


}