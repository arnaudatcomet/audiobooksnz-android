package com.audiobookz.nz.app.mylibrary.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import com.audiobookz.nz.app.util.HOUR_MILI_SEC
import com.audiobookz.nz.app.util.MINUTE_MILI_SEC
import io.audioengine.mobile.DownloadRequest
import io.audioengine.mobile.DownloadStatus
import java.util.concurrent.TimeUnit

class MyLibraryViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    var cloudBookResult = MediatorLiveData<Result<MutableMap<String, List<Any>>?>>()
    var isLatest: Boolean? = false
    var timeRemaining = ""

    fun getCloudBook(page: Int, pageSize: Int) {
        cloudBookResult.addSource(repository.getCloudBook(page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CLOUDBOOK_PAGE_SIZE) {
                    isLatest = true
                }
                val map: MutableMap<String, List<Any>> = mutableMapOf()
                map["cloudList"] = value.data
                repository.getLocalBookList(
                    { downloadStatus -> map["localList"] = downloadStatus },
                    DownloadStatus.DOWNLOADED
                )
                cloudBookResult.value = Result.success(map)
            }
        }
    }

    fun getPositionPlay(bookId: Int, chapter: Int): Long {
        return repository.getSavePositionPlay(bookId, chapter)
    }

    fun getBookChapterSize(bookId: Int): Int {
        return repository.getBookChapterSize(bookId)

    }

    fun getBookDuration(bookId: Int): Long {
        return repository.getBookDuration(bookId)
    }

    fun calculateRemainingTime(id: Int, duration: Int): Int {
        var bookSize = getBookChapterSize(id)
        var chapterCurrent = repository.getSaveBookCurrentChapter(id)
        var remainingTime = 0

        for (i in 0..bookSize) {
            //sum only before chapter and current play
            if (i <= chapterCurrent) {
                remainingTime += getPositionPlay(id, i).toInt()
            }
        }

        var timeLeft = (duration - remainingTime)
        var hourLeft = (timeLeft / HOUR_MILI_SEC)
        var minLeft = (timeLeft / MINUTE_MILI_SEC)
        var minMinuteHour = minLeft - (hourLeft * 60)
        var secLeft =
            TimeUnit.MILLISECONDS.toSeconds(timeLeft.toLong()) % TimeUnit.MINUTES.toSeconds(1)

        if (timeLeft > 0) {
            if (hourLeft != 0) {
                timeRemaining = "$hourLeft hour $minMinuteHour minute"
            } else if (minLeft != 0) {
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