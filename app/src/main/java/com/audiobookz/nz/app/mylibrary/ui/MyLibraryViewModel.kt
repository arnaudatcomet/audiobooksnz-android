package com.audiobookz.nz.app.mylibrary.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.featured.data.Featured
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadRequest
import io.audioengine.mobile.DownloadStatus

class MyLibraryViewModel @Inject constructor(private val repository: MyLibraryRepository) : ViewModel() {
    var downloadResult = MutableLiveData<DownloadEvent>()
    var contentStatusResult = MutableLiveData<DownloadStatus>()
    var listLocalBookResult = MutableLiveData<List<String>>()
    var cloudBookResult = MediatorLiveData<Result<MutableMap<String, List<Any>>?>>()
   // val cloudBookResult = MediatorLiveData<Result<List<CloudBook>>>()
    var isLatest: Boolean? = false

    fun getCloudBook(page: Int, pageSize: Int) {
        cloudBookResult.addSource(repository.getCloudBook(page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CLOUDBOOK_PAGE_SIZE) {
                    isLatest = true
                }
                val map: MutableMap<String, List<Any>> = mutableMapOf()
                map.put("cloudList",value.data)
                repository.getLocalBookList({downloadStatus ->map.put("localList", downloadStatus)},DownloadStatus.DOWNLOADED)
                cloudBookResult.value = Result.success(map)
            }
        }
    }
    fun download(title: String, contentId: String, licenseId: String) {
        repository.downloadAudiobook({downloadEvent ->downloadResult.postValue(downloadEvent)},title,contentId,licenseId)
    }

    fun getContentStatus(contentId :String) {
        repository.getContentStatus({downloadStatus ->contentStatusResult.postValue(downloadStatus)},contentId)
    }
    fun deleteContent(contentId :String,licenseId: String) {
        repository.deleteAudiobook(contentId, licenseId)
    }
    fun getLocalBookList(){
        repository.getLocalBookList({downloadStatus ->listLocalBookResult.postValue(downloadStatus)},DownloadStatus.DOWNLOADED)
    }


}