package com.audiobookz.nz.app.more.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.more.data.MoreRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.more.data.WishListData

class MoreViewModel @Inject constructor(private val repository: MoreRepository) : ViewModel() {
    var resultRemoveWishList = MediatorLiveData<Result<WishListData>>()
    var resultGetWishList = MediatorLiveData<Result<List<WishListData>>>()
    var page = 1
    var pageSize = 20

    fun getWishList() {
        resultGetWishList.addSource(
            repository.getWishList(page, pageSize)
        ) { value -> resultGetWishList.value = value }
    }

    fun removeWishList(BookId: Int) {
        var requestBookId = RequestBody.create(MediaType.parse("text/plain"), BookId.toString())
        resultRemoveWishList.addSource(
            repository.removeWishList(requestBookId)
        ) { value -> resultRemoveWishList.value = value }
    }

}