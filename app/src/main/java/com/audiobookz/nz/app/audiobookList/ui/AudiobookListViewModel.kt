package com.audiobookz.nz.app.audiobookList.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.AUDIOBOOKLIST_PAGE_SIZE
import javax.inject.Inject

class AudiobookListViewModel @Inject constructor(private val repository: AudiobookListRepository) :
    ViewModel() {
    // Need to inject the data we want
    val bookListResult = MediatorLiveData<Result<List<Audiobook>?>>()
    val searchListResult = MediatorLiveData<Result<List<Audiobook>?>>()
    var isLatest: Boolean? = false

    fun fetchCategory(filterId: Int, page: Int, pageSize: Int, lang: String, code: String) {
        bookListResult.addSource(repository.bookList(filterId, lang, page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < AUDIOBOOKLIST_PAGE_SIZE) {
                    isLatest = true
                }
                var result = filterCountry(code,value)
                bookListResult.value = result
            }
        }
    }

    fun fetchSearch(filter: String, page: Int, pageSize: Int, code: String) {
        searchListResult.addSource(repository.searchList(filter, page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < AUDIOBOOKLIST_PAGE_SIZE) {
                    isLatest = true
                }
                var result = filterCountry(code,value)
                searchListResult.value = result
            }
        }
    }

    fun filterCountry(code: String, rawResult: Result<List<Audiobook>>): Result<List<Audiobook>?> {
        var result = rawResult.data?.filter { item ->
            item.audioengine_data?.active_products?.retail!!.any {
                it.territories!!.any {
                    it == code
                }
            }
        }

        return Result.success(result)
    }

}