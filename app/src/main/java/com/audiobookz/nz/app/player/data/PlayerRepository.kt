package com.audiobookz.nz.app.player.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.cownDownTimerSleepTime
import com.audiobookz.nz.app.data.resulObservableData
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import com.audiobookz.nz.app.mylibrary.data.AudioEngineDataSource
import io.audioengine.mobile.Chapter
import io.audioengine.mobile.PlaybackEvent
import io.audioengine.mobile.PlayerState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val remoteSource: PlayerRemoteDataSource,
    private val audioEngineDataSource: AudioEngineDataSource,
    private val sharePref: SharedPreferencesService
) {

    fun pauseAudioBook() = audioEngineDataSource.pause()
    fun resumeAudioBook() = audioEngineDataSource.resume()
    fun nextChapterOfBook() = audioEngineDataSource.nextChapter()
    fun previousChapterOfBook() = audioEngineDataSource.previousChapter()
    fun setSpeed(speed: Float) = audioEngineDataSource.setSpeed(speed)
    fun seekTo(position: Long) = audioEngineDataSource.seekTo(position)
    fun getPosition() = audioEngineDataSource.getPosition()
    fun getCurrentChapter() = audioEngineDataSource.getCurrentChapter()
    fun getCurrentSpeed() = audioEngineDataSource.getCurrentSpeed()

    fun getSaveBookCurrentChapter(contentId: Int): Int {
        return sharePref.getSaveBookCurrentChapter(contentId)
    }

    fun savePositionPlay(position: Long, contentId: Int, chapter: Int) {
        sharePref.savePositionPlay(position, contentId, chapter)
    }
    fun saveBookCurrentChapter(contentId: Int, chapter: Int) {
        sharePref.saveBookCurrentChapter(contentId, chapter)
    }

    fun saveBookReadComplete(contentId: Int, boolean: Boolean) {
        sharePref.saveBookReadComplete(contentId, boolean)
    }

    fun saveBookChapterSize(contentId: Int, size: Int) {
        sharePref.saveBookChapterSize(contentId, size)
    }

    fun saveBookDuration(contentId: Int, duration: Long) {
        sharePref.saveBookDuration(contentId, duration)
    }

    fun getBookDuration(bookId: Int): Long {
        return sharePref.getBookDuration(bookId)
    }

    fun getsaveBookReadComplete(contentId: Int): Boolean {
        return sharePref.getsaveBookReadComplete(contentId)
    }

    fun getSleepTime(): Long {
        return sharePref.getSleepTime()
    }

    fun getSavePositionPlay(contentId: Int, chapter: Int): Long {
        return sharePref.getSavePositionPlay(contentId, chapter)
    }

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

    fun getChapterBooks(contentId: String, callback: (List<Chapter>) -> Unit) =
        resulObservableData(
            networkCall = audioEngineDataSource.getChapterLists(contentId),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

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

    fun getPlayerState(callback: (PlayerState) -> Unit) =
        resulObservableData(
            networkCall = audioEngineDataSource.getPlayerState(),
            onDownloading = { callback(it) },
            onPartComplete = {},
            onComplete = {},
            onDataError = {}
        )

    fun postChapterPosition(cloudBookID: Int, chapter: Int, position: Long, part: Int) =
        resultSimpleLiveData(
            networkCall = { remoteSource.postChapterPosition(cloudBookID, chapter, position, part) },
            saveCallResult = {},
            onCallSuccess = {})

    fun getBookmarks(cloudBookId:Int,page:Int,pageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getBookmarks(cloudBookId,page,pageSize) }
    )

    fun postBookmarks(cloudBookId: Int, chapter: RequestBody, position: RequestBody, subtitle: RequestBody, part: RequestBody, title: RequestBody) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.postBookmarks(cloudBookId, chapter,position,subtitle,part,title) }
    )

    fun deleteBookmark(bookmarkId:Int) = remoteSource.deleteBookmark(bookmarkId)

    fun updateBookmark(bookmarkId: Int, title:String)  = resultFetchOnlyLiveData(
       networkCall = { remoteSource.updateBookmark(bookmarkId,title)}
    )

    fun postBookReview(bookId: Int, comment: String, statification: Float, story: Float, narration: Float)  = resultFetchOnlyLiveData(
        networkCall = { remoteSource.postBookReview(bookId,comment,statification,story,narration)}
    )
}