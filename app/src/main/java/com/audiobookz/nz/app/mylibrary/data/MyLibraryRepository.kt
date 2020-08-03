package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.*
import com.audiobookz.nz.app.util.DOWNLOAD_COMPLETE
import io.audioengine.mobile.*
import javax.inject.Inject

class MyLibraryRepository @Inject constructor(
    private val remoteSource: MyLibraryRemoteDataSource,
    private val sessionDataDao: SessionDataDao,
    private val audioEngineDataSource: AudioEngineDataSource,
    private val sharePref: SharedPreferencesService,
    private val localBookDataDao: LocalBookDataDao,
    private val chapterDataDao: ChapterDataDao
) {
    fun getCloudBook(Page: Int, PageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCloudBook(Page, PageSize) }
    )
    fun saveChapterData(chapterId:Int,contentId:Int,url:String,imageUrl: String)= resultLocalSaveOnlyLiveData(
        saveCallResult = {chapterDataDao.inserChapterData(ChapterData(chapterId,contentId,imageUrl,url))}
    )

    val getLocalbook =
        resultLocalGetOnlyLiveData(databaseQuery = { localBookDataDao.getLocalBookData() })

    fun saveDetailBook(
        id: Int,
        bookId:String,
        contentId: String,
        title: String,
        licenseId: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) = resultLocalSaveOnlyLiveData(
        saveCallResult = {
            localBookDataDao.insertLocalBookData(
                LocalBookData(
                    id,
                    bookId,
                    contentId,
                    title,
                    imageUrl,
                    licenseId,
                    narrators,
                    authors
                )
            )
        }
    )

    fun getSession() = resultLiveData(
        networkCall = { remoteSource.fetchSession() },
        saveCallResult = { sessionDataDao.insertSessionData(it) },
        databaseQuery = { sessionDataDao.getSessionData() }
    )

    fun downloadAudiobook(
        callback: (DownloadEvent) -> Unit,
        id: Int, bookId:String,
        contentId: String,
        licenseId: String,
        title: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) = resulObservableData(
        networkCall = audioEngineDataSource.download(contentId, licenseId),
        onDownloading = { callback(it) },
        onPartComplete = {
            localBookDataDao.insertLocalBookData(
                LocalBookData(
                    id,
                    bookId,
                    it.content?.id,
                    title,
                    imageUrl,
                    licenseId,
                    narrators,
                    authors
                )
            )
        },
        onComplete = { audioEngineDataSource.notifySimpleNotification(title, DOWNLOAD_COMPLETE) },
        onDataError = {}
    )

    fun getBookChapterSize(bookId: Int): Int {
        return sharePref.getBookChapterSize(bookId)
    }

    fun getBookDuration(bookId: Int): Long {
        return sharePref.getBookDuration(bookId)
    }

    fun getSavePositionPlay(bookId: Int, chapter: Int): Long {
        return sharePref.getSavePositionPlay(bookId, chapter)
    }

    fun getSaveBookCurrentChapter(bookId: Int): Int {
        return sharePref.getSaveBookCurrentChapter(bookId)
    }

    fun getContentStatus(callback: (DownloadStatus) -> Unit, contentId: String) =
        resulObservableData(
            networkCall = audioEngineDataSource.getContentStatus(contentId),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

    fun deleteAudiobook(contentId: String, licenseId: String) {
        localBookDataDao.deleteById(contentId.toInt())
        audioEngineDataSource.delete(contentId, licenseId);
    }

    fun cancelDownload(downloadId: String) = audioEngineDataSource.cancelDownload(downloadId)

    fun getLocalBookList(callback: (List<String>) -> Unit, status: DownloadStatus) =
        resulObservableData(
            networkCall = audioEngineDataSource.getLocalBook(status),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

    fun getMultiValueCurrentBook() = sharePref.getMultiValueCurrentBook()


}