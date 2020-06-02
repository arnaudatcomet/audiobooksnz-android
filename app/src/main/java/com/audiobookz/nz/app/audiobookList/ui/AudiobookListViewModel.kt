package com.audiobookz.nz.app.audiobookList.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import javax.inject.Inject

class AudiobookListViewModel @Inject constructor(private val repository: AudiobookListRepository) : ViewModel() {
    // Need to inject the data we want
    var bookListResult = MediatorLiveData<Result<List<Audiobook>>>()
    var isLatest: Boolean? = false

    fun fetchCategory(filterId:Int,page: Int, pageSize: Int,lang:String) {
        bookListResult.addSource(repository.bookList(filterId,lang,page,pageSize)){ value->
            if (value.data?.size != null) {
                if (value.data.size < CATEGORY_PAGE_SIZE) {
                    isLatest = true
                }
                bookListResult.value = value
            }
        }
  }
}