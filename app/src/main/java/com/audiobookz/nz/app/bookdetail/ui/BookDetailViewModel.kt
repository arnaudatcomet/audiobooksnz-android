package com.audiobookz.nz.app.bookdetail.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.bookdetail.data.BookDetailRepository
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.audiobookz.nz.app.util.REVIEW_PAGE_SIZE
import javax.inject.Inject

class BookDetailViewModel @Inject constructor(private val repository: BookDetailRepository) : ViewModel() {
    lateinit var bookId: String
    val bookData by lazy {   repository.bookDetailData(bookId.toInt())}


    var reviewResult = MediatorLiveData<Result<List<BookReview>>>()
    var page: Int? = 1
    var isLatest: Boolean? = false
    fun fetchReview(page: Int, pageSize: Int) {
        reviewResult.addSource(repository.bookReviewData(bookId.toInt(),page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < REVIEW_PAGE_SIZE) {
                    isLatest = true
                }
                reviewResult.value = getBestResult(reviewResult.value,value)
            }
        }
    }
    fun getBestResult(oldResult: Result<List<BookReview>>?, newResult: Result<List<BookReview>>): Result<List<BookReview>>? {
        var bestResult=  oldResult.let {
                list->
            newResult.data?.let { it1 -> list?.data?.plus(it1) }
        }
        if(bestResult!=null)
        {
            return Result.success(bestResult);
        }
        return newResult;
    }
}