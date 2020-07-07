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
    private val localBookDataDao: LocalBookDataDao
) {
    fun getCloudBook(Page: Int, PageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getCloudBook(Page, PageSize) }
    )

    val getLocalbook =
        resultLocalGetOnlyLiveData(databaseQuery = { localBookDataDao.getLocalBookData() })

    fun saveDetailBook(
        id: Int,
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
        id: Int,
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

    fun setSleepTimer(countTimer: Long) = cownDownTimerSleepTime(
        countTimer,
        onComplete = {
            audioEngineDataSource.notifySimpleNotification(
                "Sleep Time",
                "Player is pause"
            )
            pauseAudioBook()
            sharePref.deleteCountTime()
        },
        saveCountTimeToShare = { sharePref.saveSleepTime(it) }
    )

    fun savePositionPlay(position: Long, bookId: Int, chapter: Int) {
        sharePref.savePositionPlay(position, bookId, chapter)
    }

    fun saveBookChapterSize(bookId: Int, size: Int) {
        sharePref.saveBookChapterSize(bookId, size)
    }

    fun saveBookDuration(bookId: Int, duration: Long) {
        sharePref.saveBookDuration(bookId, duration)
    }

    fun saveBookCurrentChapter(bookId: Int, chapter: Int) {
        sharePref.saveBookCurrentChapter(bookId, chapter)
    }

    fun saveBookReadComplete(bookId: Int, boolean: Boolean) {
        sharePref.saveBookReadComplete(bookId, boolean)
    }

    fun getBookChapterSize(bookId: Int): Int {
        return sharePref.getBookChapterSize(bookId)

    }

    fun getBookDuration(bookId: Int): Long {
        return sharePref.getBookDuration(bookId)
    }

    fun getSaveBookCurrentChapter(bookId: Int): Int {
        return sharePref.getSaveBookCurrentChapter(bookId)
    }

    fun getsaveBookReadComplete(bookId: Int): Boolean {
        return sharePref.getsaveBookReadComplete(bookId)
    }

    fun getSleepTime(): Long {
        return sharePref.getSleepTime()
    }

    fun getSavePositionPlay(bookId: Int, chapter: Int): Long {
        return sharePref.getSavePositionPlay(bookId, chapter)
    }


    fun playAudioBook(
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
    )

    fun pauseAudioBook() = audioEngineDataSource.pause()
    fun resumeAudioBook() = audioEngineDataSource.resume()

    fun getChapterBooks(contentId: String, callback: (List<Chapter>) -> Unit) =
        resulObservableData(
            networkCall = audioEngineDataSource.getChapterLists(contentId),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

    fun getPlayerState(callback: (PlayerState) -> Unit) =
        resulObservableData(
            networkCall = audioEngineDataSource.getPlayerState(),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )


    fun nextChapterOfBook() = audioEngineDataSource.nextChapter()
    fun previousChapterOfBook() = audioEngineDataSource.previousChapter()
    fun setSpeed(speed: Float) = audioEngineDataSource.setSpeed(speed)
    fun getPosition() = audioEngineDataSource.getPosition()
    fun seekTo(position: Long) = audioEngineDataSource.seekTo(position)
    fun getCurrentChapter() = audioEngineDataSource.getCurrentChapter()
    fun getCurrentSpeed() = audioEngineDataSource.getCurrentSpeed()

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

    fun postChapterPosition(bookID:Int, chapter:Int, position:Long, part:Int)=resultSimpleLiveData(
        networkCall = { remoteSource.postChapterPosition(bookID,chapter,position,part) },
        saveCallResult = {},
        onCallSuccess = {})
}