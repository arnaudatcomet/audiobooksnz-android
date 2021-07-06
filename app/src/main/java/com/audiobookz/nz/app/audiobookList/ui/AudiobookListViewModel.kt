package com.audiobookz.nz.app.audiobookList.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.ConversionEvent
import javax.inject.Inject

class AudiobookListViewModel @Inject constructor(private val repository: AudiobookListRepository) :
    ViewModel() {
    // Need to inject the data we want
    val bookListResult = MediatorLiveData<Result<List<Audiobook>?>>()
    val searchListResult = MediatorLiveData<Result<List<Audiobook>?>>()
    var isLatest: Boolean? = false
    var pageCount: Int? = 1
    var stackBook: List<Audiobook>? = null

    fun showListAnalytic(filterId: Int){
        repository.addAnalytic(ConversionEvent.view_item_list, "View Item List $filterId")
    }

    fun showSearchAnalytic(filter: String){
        repository.addAnalytic(ConversionEvent.view_search_results, "View Search Results $filter")
    }

    fun fetchCategory(filterId: Int, page: Int, pageSize: Int, lang: String, code: String) {
        bookListResult.addSource(repository.bookList(filterId, lang, page, pageSize)) { value ->
            if (value.data?.size != null) {
                var resultAddBook = fetchAddBook(stackBook, value.data) as List<Audiobook>
                stackBook = resultAddBook
                var resultFilter = filterCountry(code, resultAddBook)
                bookListResult.value = Result.success(resultFilter)
            }
        }
        repository.addAnalytic(ConversionEvent.view_item_list, "View Item List $filterId")
    }

    fun fetchSearch(filter: String, page: Int, pageSize: Int, code: String) {
        searchListResult.addSource(repository.searchList(filter, page, pageSize)) { value ->
            if (value.data?.size != null) {
                var resultAddBook = fetchAddBook(stackBook, value.data) as List<Audiobook>
                stackBook = resultAddBook
                var resultFilter = filterCountry(code, resultAddBook)
                searchListResult.value = Result.success(resultFilter)
            }
        }
//        repository.addAnalytic(ConversionEvent.view_search_results, "View Search Result")
    }

    private fun filterCountry(code: String, rawResult: List<Audiobook>): List<Audiobook>? {
        return rawResult.filter { item ->
            item.audioengine_data?.active_products?.retail!!.any { retailModel ->
                retailModel.territories!!.any {
                    it == code
                }
            }
        }
    }

    private fun fetchAddBook(
        stackBook: List<Audiobook>?,
        incomingBook: List<Audiobook>
    ): List<Audiobook>? {

        if (stackBook != null && stackBook.isNotEmpty()) {
            if (stackBook.takeLast(50).last().id == incomingBook.last()?.id) {
                isLatest = true
                return stackBook
            }
        }

        var mergeData = stackBook.let { list ->
            incomingBook.let { it1 -> list?.plus(it1) }
        }
        if (mergeData != null) {
            return mergeData
        }

        return incomingBook
    }
}