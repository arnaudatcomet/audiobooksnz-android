package com.audiobookz.nz.app.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.player.data.BookmarksData
import com.audiobookz.nz.app.player.data.PlayerRepository
import com.audiobookz.nz.app.player.data.SynPositionData
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
    var getPlayBackPositionResult = MediatorLiveData<Result<SynPositionData>>()
    val sessionData by lazy { repository.getSession() }
    var getBookmarksResult = MediatorLiveData<Result<List<BookmarksData>>>()
    var postBookmarksResult = MediatorLiveData<Result<BookmarksData>>()
    var updateBookmarksResult = MediatorLiveData<Result<BookmarksData>>()
    var postBookReviewResult = MediatorLiveData<Result<BookReview>>()
    lateinit var mainHandler: Handler
    lateinit var statusPlayer: String

    fun playAudioBook(
        cloudBookId: Int,
        firstChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        //isFirstPlay??
        var chapterCurrent = repository.getSaveBookCurrentChapter(contentId.toInt())
        var partCurrent = repository.getSaveBookCurrentPart(contentId.toInt())
        var positionCurrent = repository.getSavePositionPlay(contentId.toInt(), chapterCurrent)
        var isComplete = repository.getSaveBookReadComplete(contentId.toInt())

        when {
            isComplete -> {
                repository.saveBookReadComplete(contentId.toInt(), false)
                repository.playAudioBook(
                    //cloudBookId,
                    firstChapter, contentId, licenseId, partNumber, 0
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
            chapterCurrent == 0 -> {
                repository.playAudioBook(
                    //cloudBookId,
                    firstChapter, contentId, licenseId, partNumber, positionCurrent
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
            else -> {

                handlerTimer(cloudBookId,contentId.toInt())
                repository.playAudioBook(
                    //cloudBookId,
                     chapterCurrent, contentId, licenseId, partCurrent, positionCurrent
                ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
            }
        }
    }

    fun chooseNewChapter(
      //  cloudBookId: Int,
        targetChapter: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int
    ) {
        repository.playAudioBook(
           // cloudBookId,
            targetChapter, contentId, licenseId, partNumber, 0
        ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
    }

    fun playFromBookmark(
        //cloudBookId: Int,
        Chapter: Int,
        contentId: String,
        licenseId: String,
        partNumber: Int,
        position: Long
    ) {
        repository.playAudioBook(
           // cloudBookId,
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

    fun getCurrentSpeed(): Float? {
        return repository.getCurrentSpeed()
    }

    fun getPlayerState() {
        repository.getPlayerState { state -> playerStateResult.postValue(state) }
    }

    fun getPlayerStateForPost() {
        repository.getPlayerState { state -> statusPlayer = state.name }
    }

    fun getBookTotalDuration(bookId: Int): Long {
        return repository.getBookTotalDuration(bookId)
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

    fun saveBookSize(contentId: Int, size: Int) {
        repository.saveBookChapterSize(contentId, size)
    }

    fun saveBookTotalDuration(contentId: Int, totalDuration: Long) {
        repository.saveBookTotalDuration(contentId, totalDuration)
    }

    fun saveBookDuration(contentId: Int, totalDuration: Long) {
        repository.saveBookDuration(contentId, totalDuration)
    }

    fun saveCurrentChapter(contentId: Int, chapter: Int) {
        repository.saveBookCurrentChapter(contentId, chapter)
    }
    fun saveCurrentPart(contentId: Int, part: Int) {
        repository.saveBookCurrentPart(contentId, part)
    }

    fun saveBookReadComplete(contentId: Int) {
        repository.saveBookReadComplete(contentId, true)
    }

    fun handlerTimer(cloudBookId: Int, contentId:Int){
        mainHandler = Handler(Looper.getMainLooper())
        repository.getPlayerState { state -> playerStateResult.postValue(state) }

        var timerPlayerGetSynTask  = object : Runnable {
            override fun run() {

                getPlayerStateForPost()
                if (statusPlayer.equals("PLAYING")){
                    postSynPlayBackPosition(cloudBookId,contentId)
                }
                mainHandler.postDelayed(this, 10000)
            }
        }
        mainHandler.post(timerPlayerGetSynTask)

    }

    fun postSynPlayBackPosition(cloudBookId: Int, contentId: Int) {
        var chapterCurrent = repository.getSaveBookCurrentChapter(contentId)
        var partCurrent = repository.getSaveBookCurrentPart(contentId)
        var positionCurrent = repository.getSavePositionPlay(contentId, chapterCurrent)

        var requestCall = repository.postSyncPlayBackPosition(
            cloudBookId,
            chapterCurrent,
            positionCurrent,
            partCurrent
        )
        requestCall.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //do nothing
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    println("### post syn done ###")
                }
            }
        })}

    fun getSyncPlayBackPosition(cloudBookId: Int) {
        getPlayBackPositionResult.addSource(
            (repository.getSyncPlayBackPosition(cloudBookId))
        ) { value -> getPlayBackPositionResult.value = value }
    }

    fun getPlayBackPosition(cloudBookId: Int,firstChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        getPlayBackPositionResult.addSource(
            (repository.getSyncPlayBackPosition(cloudBookId))
        ) {
                value -> getPlayBackPositionResult.value = value
            playAudioBook(
                cloudBookId,
                firstChapter,
                contentId,
                licenseId,
                partNumber
            )
        }
    }

    fun getSavePositionPlay(contentId: Int, chapter: Int): Long {
        return repository.getSavePositionPlay(contentId,chapter)
    }

    fun getSaveBookCurrentChapter(contentId: Int): Int {
        return repository.getSaveBookCurrentChapter(contentId)
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

    fun updateBookmark(bookmarkId: Int, title: String) {
        updateBookmarksResult.addSource(repository.updateBookmark(bookmarkId, title)) { value ->
            updateBookmarksResult.value = value
        }
    }

    fun postBookReview(
        bookId: Int,
        comment: String,
        statification: Float,
        story: Float,
        narration: Float
    ) {
        postBookReviewResult.addSource(
            repository.postBookReview(
                bookId,
                comment,
                statification,
                story,
                narration
            )
        ) { value ->
            postBookReviewResult.value = value
        }
    }

    fun saveMultiValueCurrentBook(bookDetail: ArrayList<String>) {
        repository.saveMultiValueCurrentBook(bookDetail)
    }
}