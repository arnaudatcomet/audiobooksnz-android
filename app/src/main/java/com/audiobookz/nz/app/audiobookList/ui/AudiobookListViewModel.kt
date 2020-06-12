package com.audiobookz.nz.app.audiobookList.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import javax.inject.Inject

class AudiobookListViewModel @Inject constructor(private val repository: AudiobookListRepository) :
    ViewModel() {
    // Need to inject the data we want
    var bookListResult = MediatorLiveData<Result<List<Audiobook>>>()
    var searchListResult = MediatorLiveData<Result<List<Audiobook>>>()
    var isLatest: Boolean? = false

    fun fetchCategory(filterId: Int, page: Int, pageSize: Int, lang: String) {
        bookListResult.addSource(repository.bookList(filterId, lang, page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CATEGORY_PAGE_SIZE) {
                    isLatest = true
                }
                bookListResult.value = checkReloadData(bookListResult.value,value)
            }
        }
    }

    fun fetchSearch(filter: String, page: Int, pageSize: Int) {
        searchListResult.addSource(repository.searchList(filter, page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CATEGORY_PAGE_SIZE) {
                    isLatest = true
                }
                searchListResult.value = checkReloadData(searchListResult.value,value)
            }
        }
    }

    private fun checkReloadData(
        oldResult: Result<List<Audiobook>>?,
        newResult: Result<List<Audiobook>>
    ): Result<List<Audiobook>>? {
        if (oldResult?.data?.get(0)?.id == newResult.data?.get(0)?.id) {
            return oldResult
        }
        var bestReload = oldResult.let { list ->
            newResult.data?.let { it1 -> list?.data?.plus(it1) }
        }
        if (bestReload != null) {
            return Result.success(bestReload);
        }
        return newResult
    }
}