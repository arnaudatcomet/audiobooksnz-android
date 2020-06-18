package com.audiobookz.nz.app.mylibrary.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE

class MyLibraryViewModel @Inject constructor(private val repository: MyLibraryRepository) : ViewModel() {

    val cloudBookResult = MediatorLiveData<Result<List<CloudBook>>>()
    var isLatest: Boolean? = false

    fun getCloudBook(page: Int, pageSize: Int) {
        cloudBookResult.addSource(repository.getCloudBook(page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CLOUDBOOK_PAGE_SIZE) {
                    isLatest = true
                }
                cloudBookResult.value = value
            }
        }
    }

}