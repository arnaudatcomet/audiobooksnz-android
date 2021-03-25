package com.audiobookz.nz.app.mylibrary.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import com.audiobookz.nz.app.util.HOUR_MILI_SEC
import com.audiobookz.nz.app.util.MINUTE_MILI_SEC
import io.audioengine.mobile.DownloadRequest
import io.audioengine.mobile.DownloadStatus
import java.util.concurrent.TimeUnit

class MyLibraryViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    val getMultiValueCurretBook = repository.getMultiValueCurrentBook()
    var cloudBookResult = MediatorLiveData<Result<MutableMap<String, List<Any>>?>>()
    var oldCloudBook: List<CloudBook>? = null
    var isLatest: Boolean? = false
    var timeRemaining = ""
    var pageCount: Int? = 1

    fun getCloudBook(page: Int, pageSize: Int, searchText: String) {
        cloudBookResult.addSource(repository.getCloudBook(page, pageSize)) { value ->
            if (value.data?.size != null) {

                val map: MutableMap<String, List<Any>> = mutableMapOf()
                var resultFetch = fetchMoreCloudBook(oldCloudBook, value.data!!) as List<CloudBook>
                oldCloudBook = resultFetch

                map["cloudList"] = resultFetch.filter {
                    it.audiobook?.title!!.contains(
                        searchText,
                        ignoreCase = true
                    )
                }
                repository.getLocalBookList(
                    { downloadStatus -> map["localList"] = downloadStatus },
                    DownloadStatus.DOWNLOADED
                )
                cloudBookResult.value = Result.success(map)

            }
        }
    }

    fun fetchMoreCloudBook(oldData: List<CloudBook>?, newData: List<CloudBook>): List<CloudBook>? {

        if (oldData != null && oldData.isNotEmpty()) {

            if (oldData.takeLast(10).last().id == newData.last()?.id) {
                isLatest = true
                return oldData
            }
        }

        var mergeData = oldData.let { list ->
            newData.let { it1 -> list?.plus(it1) }
        }
        if (mergeData != null) {
            return mergeData
        }
        return newData
    }

    fun getPositionPlay(bookId: Int, chapter: Int): Long {
        return repository.getSavePositionPlay(bookId, chapter)
    }

    fun getBookTotalDuration(bookId: Int): Long {
        return repository.getBookTotalDuration(bookId)
    }

    fun calculateRemainingTime(contentId: Int): Int {
        var bookSize = repository.getBookChapterSize(contentId)
        var chapterCurrent = repository.getSaveBookCurrentChapter(contentId)
        var durationTotal = repository.getBookTotalDuration(contentId)
        var remainingTime = 0

        for (i in 0..bookSize) {
            //sum only before chapter and current play
            if (i <= chapterCurrent) {
                remainingTime += getPositionPlay(contentId, i).toInt()
            }
        }

        var timeLeft = (durationTotal - remainingTime)
        var hourLeft = (timeLeft / HOUR_MILI_SEC)
        var minLeft = (timeLeft / MINUTE_MILI_SEC)
        var minMinuteHour = minLeft - (hourLeft * 60)
        var secLeft =
            TimeUnit.MILLISECONDS.toSeconds(timeLeft) % TimeUnit.MINUTES.toSeconds(1)

        if (timeLeft > 0) {
            if (hourLeft != 0L) {
                timeRemaining = "$hourLeft hour $minMinuteHour minute"
            } else if (minLeft != 0L) {
                timeRemaining = "$minLeft minute"
            } else if (secLeft != 0L) {
                timeRemaining = "less than minute"
            } else {
                timeRemaining = "0 minute"
            }
        } else {
            timeRemaining = "0 minute"
        }

        return remainingTime
    }

}