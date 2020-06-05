package com.audiobookz.nz.app.appbar


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookDetailRepository
import javax.inject.Inject

class AppbarViewModel @Inject constructor(private val repository: BookDetailRepository) : ViewModel() {
    val count by lazy {   repository.countCart()}
}
