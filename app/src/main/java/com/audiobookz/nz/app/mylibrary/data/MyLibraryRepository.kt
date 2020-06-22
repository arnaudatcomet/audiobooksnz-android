package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.data.resulObservableData
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.util.DOWNLOAD_COMPLETE
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadStatus
import io.audioengine.mobile.PlaybackEvent
import javax.inject.Inject

class MyLibraryRepository @Inject constructor(private val remoteSource: MyLibraryRemoteDataSource,private val sessionDataDao: SessionDataDao,private val audioEngineDataSource: AudioEngineDataSource
) {
    fun getCloudBook(Page:Int,PageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCloudBook(Page,PageSize) }
    )

    fun getSession() = resultLiveData(
        networkCall = { remoteSource.fetchSession() },
        saveCallResult = {sessionDataDao.insertSessionData(it)},
        databaseQuery = {sessionDataDao.getSessionData()}
    )

    fun downloadAudiobook (
        callback: (DownloadEvent) -> Unit,
        title: String,
        contentId: String,
        licenseId: String
    ) = resulObservableData(
        networkCall = audioEngineDataSource.download(contentId,licenseId),
        onDownloading = {callback(it)},
        onPartComplete = {audioEngineDataSource.notifySimpleNotification(
            title, DOWNLOAD_COMPLETE
        )},
        onComplete = {},
        onDataError = {}
    )

    fun PlayAudioBook(chapterNumber:Int,contentId: String, licenseId: String,partNumber:Int,position:Long,callback: (PlaybackEvent) -> Unit)= resulObservableData(
        networkCall = audioEngineDataSource.playAudiobook(contentId,licenseId,partNumber,chapterNumber,position),
        onDownloading = {callback(it)},
        onPartComplete = {},
        onComplete = {},
        onDataError = {}
    );

    fun getContentStatus (callback: (DownloadStatus) -> Unit, contentId :String) = resulObservableData(
        networkCall = audioEngineDataSource.getContentStatus(contentId),
        onDownloading = {callback(it)},
        onPartComplete = {},
        onComplete = {},
        onDataError = {}
    )
    fun deleteAudiobook(contentId: String, licenseId: String) = audioEngineDataSource.delete(contentId, licenseId)

    fun cancelDownload(contentId: String, licenseId: String) = audioEngineDataSource.cancelDownload(contentId, licenseId)

    fun getLocalBookList (callback: (List<String>) -> Unit, status: DownloadStatus ) = resulObservableData(
        networkCall = audioEngineDataSource.getLocalBook(status),
        onDownloading = {callback(it)},
        onPartComplete = {},
        onComplete = {},
        onDataError = {}
    )
   // fun getLocalBookList() = remoteSource.getLocalBook(status)


}