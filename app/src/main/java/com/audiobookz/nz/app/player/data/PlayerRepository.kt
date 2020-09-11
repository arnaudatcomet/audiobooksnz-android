package com.audiobookz.nz.app.player.data

import android.os.CountDownTimer
import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.*
import com.audiobookz.nz.app.mylibrary.data.AudioEngineDataSource
import com.audiobookz.nz.app.mylibrary.data.SessionDataDao
import io.audioengine.mobile.Chapter
import io.audioengine.mobile.PlaybackEvent
import io.audioengine.mobile.PlayerState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val remoteSource: PlayerRemoteDataSource,
    private val audioEngineDataSource: AudioEngineDataSource,
    private val sharePref: SharedPreferencesService,
    private val sessionDataDao: SessionDataDao
) {
    private var oldPosition = 0L
    private var newPosition = 0L

    fun pauseAudioBook() = audioEngineDataSource.pause()
    fun resumeAudioBook() = audioEngineDataSource.resume()
    fun nextChapterOfBook() = audioEngineDataSource.nextChapter()
    fun previousChapterOfBook() = audioEngineDataSource.previousChapter()
    fun setSpeed(speed: Float) = audioEngineDataSource.setSpeed(speed)
    fun seekTo(position: Long) = audioEngineDataSource.seekTo(position)

    fun getCurrentChapter() = audioEngineDataSource.getCurrentChapter()
    fun getCurrentSpeed() = audioEngineDataSource.getCurrentSpeed()

    fun getSaveBookCurrentChapter(contentId: Int): Int {
        return sharePref.getSaveBookCurrentChapter(contentId)
    }

    fun getSaveBookCurrentPart(contentId: Int): Int {
        return sharePref.getSaveBookCurrentPart(contentId)
    }

    fun savePositionPlay(position: Long, contentId: Int, chapter: Int) {
        sharePref.savePositionPlay(position, contentId, chapter)
    }

    fun saveBookCurrentChapter(contentId: Int, chapter: Int) {
        sharePref.saveBookCurrentChapter(contentId, chapter)
    }

    fun saveBookCurrentPart(contentId: Int, part: Int) {
        sharePref.saveBookCurrentPart(contentId, part)
    }

    fun saveBookReadComplete(contentId: Int, boolean: Boolean) {
        sharePref.saveBookReadComplete(contentId, boolean)
    }

    fun saveBookChapterSize(contentId: Int, size: Int) {
        sharePref.saveBookChapterSize(contentId, size)
    }

    fun saveBookTotalDuration(contentId: Int, duration: Long) {
        sharePref.saveBookTotalDuration(contentId, duration)
    }

    fun saveBookDuration(contentId: Int, duration: Long) {
        sharePref.saveBookDuration(contentId, duration)
    }

    fun saveMultiValueCurrentBook(bookDetail: ArrayList<String>) {
        sharePref.saveMultiValueCurrentBook(bookDetail)
    }

    fun getBookTotalDuration(bookId: Int): Long {
        return sharePref.getBookTotalDuration(bookId)
    }

    fun getBookDuration(bookId: Int): Long {
        return sharePref.getBookDuration(bookId)
    }

    fun getSaveBookReadComplete(contentId: Int): Boolean {
        return sharePref.getSaveBookReadComplete(contentId)
    }

    fun getSleepTime(): Long {
        return sharePref.getSleepTime()
    }

    fun getSavePositionPlay(contentId: Int, chapter: Int): Long {
        return sharePref.getSavePositionPlay(contentId, chapter)
    }

    fun setSleepTimer(countTimer: Long) = countDownTimerSleepTime(
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
        resultObservablePlayerEngine(
            networkCall = audioEngineDataSource.getChapterLists(contentId),
            onPlaying = { callback(it) }
        )

//    fun playAudioBook(
//        cloudBookId: Int,
//        chapterNumber: Int,
//        contentId: String,
//        licenseId: String,
//        partNumber: Int,
//        position: Long,
//        callback: (PlaybackEvent) -> Unit
//    ) = resultObservablePlayerEngine(
//        networkCall = audioEngineDataSource.playAudiobook(
//            contentId,
//            licenseId,
//            partNumber,
//            chapterNumber,
//            position
//        ),
//        onPlaying = { callback(it) },
//        saveChapter = { saveBookCurrentChapter(it.content!!.id.toInt(), it.chapter!!.chapter) },
//        savePart = { saveBookCurrentPart(it.content!!.id.toInt(), it.chapter!!.part) },
//        savePosition = {
//            savePositionPlay(
//                it.position!!.toLong(),
//                it.content!!.id.toInt(),
//                it.chapter!!.chapter
//            )
//        }
//    )
    fun playAudioBook(
        chapterNumber: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int,
        position: Long,
        callback: (PlaybackEvent) -> Unit
    ) = resultObservablePlayerEngine(
        networkCall = audioEngineDataSource.playAudiobook(
            contentId,
            licenseId,
            partNumber,
            chapterNumber,
            position
        ),
        onPlaying = { callback(it) }
    )

    fun getPlayerState(callback: (PlayerState) -> Unit) =
        resultObservablePlayerEngine(
            networkCall = audioEngineDataSource.getPlayerState(),
            onPlaying = { callback(it) }

        )

    fun postSyncPlayBackPosition(cloudBookId :Int, chapter: Int, position: Long, part: Int)=
        remoteSource.postSyncPlayBackPosition(cloudBookId, chapter, position, part)

    fun getSyncPlayBackPosition(cloudBookId: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getSyncPlayBackPosition(cloudBookId) }
    )

    fun getBookmarks(cloudBookId: Int, page: Int, pageSize: Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.getBookmarks(cloudBookId, page, pageSize) }
    )

    fun postBookmarks(
        cloudBookId: Int,
        chapter: RequestBody,
        position: RequestBody,
        subtitle: RequestBody,
        part: RequestBody,
        title: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = {
            remoteSource.postBookmarks(
                cloudBookId,
                chapter,
                position,
                subtitle,
                part,
                title
            )
        }
    )

    fun deleteBookmark(bookmarkId: Int) = remoteSource.deleteBookmark(bookmarkId)

    fun updateBookmark(bookmarkId: Int, title: String) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.updateBookmark(bookmarkId, title) }
    )

    fun postBookReview(
        bookId: Int,
        comment: String,
        statification: Float,
        story: Float,
        narration: Float
    ) = resultFetchOnlyLiveData(
        networkCall = {
            remoteSource.postBookReview(
                bookId,
                comment,
                statification,
                story,
                narration
            )
        }
    )

    fun getSession() = resultLiveData(
        networkCall = { remoteSource.fetchSession() },
        saveCallResult = { sessionDataDao.insertSessionData(it) },
        databaseQuery = { sessionDataDao.getSessionData() }
    )
}