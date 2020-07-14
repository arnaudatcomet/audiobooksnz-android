package com.audiobookz.nz.app.player.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.player.data.BookmarksData
import com.audiobookz.nz.app.player.data.PlayerRepository
import com.audiobookz.nz.app.player.data.PositionData
import io.audioengine.mobile.Chapter
import io.audioengine.mobile.PlaybackEvent
import io.audioengine.mobile.PlayerState
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PlayerViewModel @Inject constructor(private val repository: PlayerRepository) :
    ViewModel() {
    var playBackResult = MutableLiveData<PlaybackEvent>()
    var listChapterResult = MutableLiveData<List<Chapter>>()
    var playerStateResult = MutableLiveData<PlayerState>()
    var currentPlay = repository.getCurrentChapter()
    var currentSleepTimer = repository.getSleepTime()
    var positionPostResult = MediatorLiveData<Result<PositionData>>()
    var getBookmarksResult = MediatorLiveData<Result<List<BookmarksData>>>()
    var postBookmarksResult = MediatorLiveData<Result<BookmarksData>>()
    var updateBookmarksResult = MediatorLiveData<Result<BookmarksData>>()
    var postBookReviewResult = MediatorLiveData<Result<BookReview>>()

    fun playAudioBook(firstChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        //isFirstPlay??
        var chapterCurrent = repository.getSaveBookCurrentChapter(contentId.toInt())
        var positionCurrent = repository.getSavePositionPlay(contentId.toInt(), chapterCurrent)
        var isComplete = repository.getsaveBookReadComplete(contentId.toInt())

        when {
            isComplete -> {
                repository.saveBookReadComplete(contentId.toInt(), false)
                repository.playAudioBook(
                    firstChapter, contentId, licenseId, partNumber, 0
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
            chapterCurrent == 0 -> {
                repository.playAudioBook(
                    firstChapter, contentId, licenseId, partNumber, positionCurrent
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
            else -> {
                repository.playAudioBook(
                    chapterCurrent, contentId, licenseId, partNumber, positionCurrent
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
        }

    }

    fun chooseNewChapter(
        targetChapter: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int
    ) {
        repository.playAudioBook(
            targetChapter, contentId, licenseId, partNumber, 0
        ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
    }

    fun playFromBookmark(
        Chapter: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int,
        position: Long
    ) {
        repository.playAudioBook(
            Chapter, contentId, licenseId, partNumber, position
        ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
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

        repository.getChapterBooks(contentId) { listChapters ->
            listChapterResult.postValue(
                listChapters
            )
        }
    }

    fun seekTo(position: Long) {
        repository.seekTo(position)
    }

    fun getCurrentSpeed(): Float {
        return repository.getCurrentSpeed()
    }

    fun getPlayerState() {
        repository.getPlayerState { state -> playerStateResult.postValue(state) }
    }

    fun getBookDuration(bookId: Int): Long {
        return repository.getBookDuration(bookId)
    }

    fun setTimeSleep(countTime: Long) {
        repository.setSleepTimer(countTime)
    }

    fun savePositionPlay(position: Long, contentId: Int, chapter: Int) {
        repository.savePositionPlay(position, contentId, chapter)
    }

    fun saveBookSize(contentId: Int, size:Int){
        repository.saveBookChapterSize(contentId, size)
    }

    fun saveBookTotalDuration(contentId:Int, totalDuration:Long){
        repository.saveBookDuration(contentId, totalDuration)
    }

    fun saveCurrentChapter(contentId: Int, chapter: Int) {
        repository.saveBookCurrentChapter(contentId, chapter)
    }

    fun saveBookReadComplete(contentId: Int) {
        repository.saveBookReadComplete(contentId, true)
    }

    fun postChapterPosition(cloudBookID: Int, chapter: Int, position: Long, part: Int) {
        positionPostResult.addSource(
            repository.postChapterPosition(
                cloudBookID,
                chapter,
                position,
                part
            )
        ) { value ->
            positionPostResult.value = value
        }
    }

    fun getBookmarks(cloudBookId: Int) {
        getBookmarksResult.addSource(
            (repository.getBookmarks(
                cloudBookId,
                1,
                50
            ))
        ) { value ->
            getBookmarksResult.value = value
        }
    }

    fun postBookmarks(
        cloudBookId: Int,
        chapter: String,
        position: String,
        subtitle: String,
        part: String,
        title: String
    ) {
        var requestChapter = RequestBody.create(MediaType.parse("text/plain"), chapter)
        var requestPosition = RequestBody.create(MediaType.parse("text/plain"), position)
        var requestSubtitle = RequestBody.create(MediaType.parse("text/plain"), subtitle)
        var requestPart = RequestBody.create(MediaType.parse("text/plain"), part)
        var requestTitle = RequestBody.create(MediaType.parse("text/plain"), title)

        postBookmarksResult.addSource(
            repository.postBookmarks(
                cloudBookId,
                requestChapter,
                requestPosition,
                requestSubtitle,
                requestPart,
                requestTitle
            )
        ) { value ->
            postBookmarksResult.value = value
        }
    }

    fun deleteBookmark(bookmarkId: Int, cloudId: Int) {
        var requestCall = repository.deleteBookmark(bookmarkId)
        requestCall.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //do nothing
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    getBookmarks(cloudId)
                }
            }
        })
    }

    fun updateBookmark(bookmarkId:Int, title:String){
        updateBookmarksResult.addSource(repository.updateBookmark(bookmarkId,title)){value ->
            updateBookmarksResult.value = value
        }
    }

    fun postBookReview(bookId: Int, comment: String, statification: Float, story: Float, narration: Float){
        postBookReviewResult.addSource(repository.postBookReview(bookId,comment,statification,story,narration)){value ->
            postBookReviewResult.value = value
        }
    }
}