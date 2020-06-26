package com.audiobookz.nz.app.mylibrary.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import javax.inject.Inject


class DeviceLibraryViewModel @Inject constructor(private val repository: MyLibraryRepository) :
    ViewModel() {
    val localBookList = repository.getLocalbook
}