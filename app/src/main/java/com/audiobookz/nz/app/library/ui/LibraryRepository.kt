package com.audiobookz.nz.app.library.ui

import com.audiobookz.nz.app.data.resulObservableData
import com.audiobookz.nz.app.util.DOWNLOAD_COMPLETE
import io.audioengine.mobile.DownloadEvent
import rx.Observer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LibraryRepository @Inject constructor(private val libraryDataSource: LibraryDataSource) {

     fun downloadAudiobook (callback: (DownloadEvent) -> Unit,title:String) = resulObservableData(
          networkCall = libraryDataSource.download(),
          onDownloading = {callback(it)},
          onPartComplete = {libraryDataSource.notifySimpleNotification(
               title,DOWNLOAD_COMPLETE)},
          onComplete = {},
          onDataError = {}
     )

     fun detele() = libraryDataSource.delete
}
