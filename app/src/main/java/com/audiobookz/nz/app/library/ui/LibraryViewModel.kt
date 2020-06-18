package com.audiobookz.nz.app.library.ui

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import io.audioengine.mobile.DownloadEngine
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadRequest
import rx.Observer
import javax.inject.Inject

class LibraryViewModel @Inject constructor(private val libraryRepository: LibraryRepository) :ViewModel() {
    var downloadResult = MutableLiveData<DownloadEvent>()
    //val data = libraryRepository.download
    var downloadStatus = MediatorLiveData<Boolean>()

    fun delete() = libraryRepository.detele()


     fun download(title:String) {
         libraryRepository.downloadAudiobook({downloadEvent ->downloadResult.postValue(downloadEvent)},title)
    }

}
