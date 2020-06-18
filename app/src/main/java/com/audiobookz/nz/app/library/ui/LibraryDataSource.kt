package com.audiobookz.nz.app.library.ui

import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.BookEngineService
import com.audiobookz.nz.app.api.NotificationService
import javax.inject.Inject

class LibraryDataSource @Inject constructor(private val bookEngineService: BookEngineService,private val notificationService: NotificationService) : BaseDataSource() {

     fun download() = bookEngineService.download()
     val delete = bookEngineService.delete()
     fun notifySimpleNotification(title:String, body:String)=notificationService.simple(title,body)

}
