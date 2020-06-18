package com.audiobookz.nz.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookDetailRepository
import com.audiobookz.nz.app.library.ui.LibraryDataSource
import com.audiobookz.nz.app.library.ui.LibraryRepository
import retrofit2.http.Body
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val repository: BookDetailRepository) : ViewModel() {
    val count by lazy {   repository.countCart()}
}
