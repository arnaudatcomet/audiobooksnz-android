package com.audiobookz.nz.app.player.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.player.data.PositionData
import io.audioengine.mobile.Chapter
import io.audioengine.mobile.PlaybackEvent
import io.audioengine.mobile.PlayerState
import javax.inject.Inject

class PlayerViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    var playBackResult = MutableLiveData<PlaybackEvent>()
    var listChapterResult = MutableLiveData<List<Chapter>>()
    var playerStateResult = MutableLiveData<PlayerState>()
    var currentPlay = repository.getCurrentChapter()
    var currentSleepTimer = repository.getSleepTime()
    var positionPostResult = MediatorLiveData<Result<PositionData>>()
    val sessionData by lazy { repository.getSession() }
    fun playAudioBook(firstChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        //isFirstPlay??
        var chapterCurrent = repository.getSaveBookCurrentChapter(contentId.toInt())
        var positionCurrent = repository.getSavePositionPlay(contentId.toInt(), chapterCurrent)
        var isComplete = repository.getsaveBookReadComplete(contentId.toInt())

        if (isComplete) {
            repository.saveBookReadComplete(contentId.toInt(), false)
            repository.playAudioBook(
                firstChapter, contentId, licenseId, partNumber, 0
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        } else if (chapterCurrent == 0) {
            repository.playAudioBook(
                firstChapter, contentId, licenseId, partNumber, positionCurrent
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        } else {
            repository.playAudioBook(
                chapterCurrent, contentId, licenseId, partNumber, positionCurrent
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        }

    }

    fun chooseNewChapter(
        targetChapter: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int
    ) {
        getPosition()?.let {
            repository.playAudioBook(
                targetChapter, contentId, licenseId, partNumber, it
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        }
    }

    fun pauseAudioBook() {
        repository.pauseAudioBook()
    }

    fun resumeAudioBook() {
        repository.resumeAudioBook()
    }

    fun nextChapter() {
        repository.nextChapterOfBook()
    }

    fun previousChapter() {
        repository.previousChapterOfBook()
    }

    fun setSpeed(speed: Float) {
        repository.setSpeed(speed)
    }

    fun getChapters(contentId: String) {
        var totalBookDuration: Long = 0
        repository.getChapterBooks(contentId) { listChapters ->
            listChapterResult.postValue(
                listChapters
            )
            if (listChapters != null) {
                for (x in listChapters) {
                    totalBookDuration += x.duration
                }
                repository.saveBookChapterSize(contentId.toInt(), listChapters.size)
                repository.saveBookDuration(contentId.toInt(), totalBookDuration)
            }
        }
    }

    fun seekTo(position: Long) {
        repository.seekTo(position)
    }

    fun getPosition(): Long? {
        return repository.getPosition()
    }

    fun getCurrentSpeed(): Float? {
        return repository.getCurrentSpeed()
    }

    fun getPlayerState() {
        repository.getPlayerState { state -> playerStateResult.postValue(state) }
    }

    fun setTimeSleep(counTime: Long) {
        repository.setSleepTimer(counTime)
    }

    fun savePositionPlay(position: Long, bookId: Int, chapter: Int) {
        repository.savePositionPlay(position, bookId, chapter)
    }

    fun saveCurrentChapter(bookId: Int, chapter: Int) {
        repository.saveBookCurrentChapter(bookId, chapter)
    }

    fun saveBookReadComplete(bookId: Int) {
        repository.saveBookReadComplete(bookId, true)
    }

    fun postChapterPosition(bookID:Int, chapter:Int, position:Long, part:Int) {
        positionPostResult.addSource(repository.postChapterPosition(bookID,chapter,position,part)) { value ->
            positionPostResult.value = value
        }
    }

}