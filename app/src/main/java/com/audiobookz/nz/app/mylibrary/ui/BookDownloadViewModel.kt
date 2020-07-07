package com.audiobookz.nz.app.mylibrary.ui

import android.os.CountDownTimer
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadStatus

class BookDownloadViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    val downloadResult = MutableLiveData<DownloadEvent>()
    var contentStatusResult = MutableLiveData<DownloadStatus>()
    var bookDetail = MediatorLiveData<Result<String>>()

    //    lateinit var timer : CountDownTimer
//    var timerData =  MutableLiveData<Long>()
//
//    fun startTimer(){
//        timer = object : CountDownTimer(30000,1000){
//            override fun onTick(millisUntilFinished: Long) {
//                timerData.value = millisUntilFinished/1000
//            }
//
//            override fun onFinish() {
//                timerData.value = 1000
//            }
//        }
//        if (timerData.value==null){
//            timer.start()
//        }
//    }
    fun saveDetailBook(
        id: Int,
        contentId: String,
        title: String,
        licenseId: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) {
        bookDetail.addSource(
            repository.saveDetailBook(
                id,
                contentId,
                title,
                licenseId,
                imageUrl,
                authors,
                narrators
            )
        ) { value ->
            bookDetail.value = value
        }
    }


    fun download(
        id: Int,
        contentId: String, licenseId: String, title: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) {

        repository.downloadAudiobook(
            { downloadEvent -> downloadResult.postValue(downloadEvent) },
            id,
            contentId,
            licenseId,
            title, imageUrl, authors, narrators
        )
    }

    fun getContentStatus(contentId: String) {
        repository.getContentStatus(
            { downloadStatus -> contentStatusResult.postValue(downloadStatus) },
            contentId
        )
    }

    fun deleteContent(contentId: String, licenseId: String) {
        repository.deleteAudiobook(contentId, licenseId)
    }

    fun cancelDownload(downloadId: String) {
        repository.cancelDownload(downloadId)
    }


}