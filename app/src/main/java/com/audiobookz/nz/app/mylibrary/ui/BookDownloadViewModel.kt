package com.audiobookz.nz.app.mylibrary.ui

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.browse.featured.data.Featured
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.mylibrary.data.LocalBookData
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadRequest
import io.audioengine.mobile.DownloadStatus

class BookDownloadViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    val downloadResult = MutableLiveData<DownloadEvent>()
    var contentStatusResult = MutableLiveData<DownloadStatus>()
    var bookDetail = MediatorLiveData<Result<LocalBookData>>()

    fun getDetailBook(
        id: String,
        title: String ) {
        bookDetail.addSource(repository.getDetailBook(id,title)) { value ->
            bookDetail.value = value
        }
    }


    fun download(contentId: String, licenseId: String) {

        repository.downloadAudiobook(
            { downloadEvent -> downloadResult.postValue(downloadEvent) },
            contentId,
            licenseId
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

    fun cancelDownload(contentId: String, licenseId: String) {
        repository.cancelDownload(contentId, licenseId)
    }



}