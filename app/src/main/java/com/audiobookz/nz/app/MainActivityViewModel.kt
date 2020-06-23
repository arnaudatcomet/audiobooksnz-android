package com.audiobookz.nz.app

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookDetailRepository
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val repository: BookDetailRepository,private val myLibraryRepository: MyLibraryRepository) : ViewModel() {
    val count by lazy {   repository.countCart()}
    val sessionId by lazy { myLibraryRepository.getSession() }
}
