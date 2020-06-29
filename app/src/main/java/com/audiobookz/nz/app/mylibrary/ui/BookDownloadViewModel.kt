package com.audiobookz.nz.app.mylibrary.ui

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

    fun saveDetailBook(
        id: String,
        title: String,
        licenseId: String,
        imageUrl: String?,
        authors: String,
        narrators: String
    ) {
        bookDetail.addSource(repository.saveDetailBook(id,title,licenseId,imageUrl,authors,narrators)) { value ->
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