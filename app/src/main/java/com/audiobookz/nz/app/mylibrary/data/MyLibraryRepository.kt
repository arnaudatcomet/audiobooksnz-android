package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.bookdetail.data.BookDetailRemoteDataSource
import com.audiobookz.nz.app.data.*
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadStatus
import io.audioengine.mobile.PlaybackEvent
import javax.inject.Inject

class MyLibraryRepository @Inject constructor(
    private val remoteSource: MyLibraryRemoteDataSource,
    private val sessionDataDao: SessionDataDao,
    private val audioEngineDataSource: AudioEngineDataSource,
    private val bookDetailDataSource: BookDetailRemoteDataSource,
    private val localBookDataDao: LocalBookDataDao
) {
    fun getCloudBook(Page: Int, PageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCloudBook(Page, PageSize) }
    )
    val getLocalbook = resultLocalGetOnlyLiveData (
        databaseQuery={localBookDataDao.getLocalBookData()}
    )

    fun saveDetailBook(
        id: String,
        title: String,
        licenseId: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) = resultLocalSaveOnlyLiveData (
        saveCallResult = {localBookDataDao.insertLocalBookData(LocalBookData(id,title,imageUrl,licenseId,narrators,authors))}
    )



    fun getSession() = resultLiveData(
        networkCall = { remoteSource.fetchSession() },
        saveCallResult = { sessionDataDao.insertSessionData(it) },
        databaseQuery = { sessionDataDao.getSessionData() }
    )

    fun downloadAudiobook(
        callback: (DownloadEvent) -> Unit,
        contentId: String,
        licenseId: String
    ) = resulObservableData(
        networkCall = audioEngineDataSource.download(contentId, licenseId),
        onDownloading = { callback(it) },
        onPartComplete = {},
        onComplete = {},
        onDataError = {}
    )

    fun PlayAudioBook(
        chapterNumber: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int,
        position: Long,
        callback: (PlaybackEvent) -> Unit
    ) = resulObservableData(
        networkCall = audioEngineDataSource.playAudiobook(
            contentId,
            licenseId,
            partNumber,
            chapterNumber,
            position
        ),
        onDownloading = { callback(it) },
        onPartComplete = {},
        onComplete = {},
        onDataError = {}
    );

    fun getContentStatus(callback: (DownloadStatus) -> Unit, contentId: String) =
        resulObservableData(
            networkCall = audioEngineDataSource.getContentStatus(contentId),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

    fun deleteAudiobook(contentId: String, licenseId: String) {
        localBookDataDao.deleteById(contentId);
        audioEngineDataSource.delete(contentId, licenseId);
    }

    fun cancelDownload(contentId: String, licenseId: String) =
        audioEngineDataSource.cancelDownload(contentId, licenseId)

    fun getLocalBookList(callback: (List<String>) -> Unit, status: DownloadStatus) =
        resulObservableData(
            networkCall = audioEngineDataSource.getLocalBook(status),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )
    // fun getLocalBookList() = remoteSource.getLocalBook(status)


}