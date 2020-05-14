package com.audiobookz.nz.app.audiobookList.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import javax.inject.Inject

class AudiobookListViewModel @Inject constructor(repository: AudiobookListRepository) : ViewModel() {
    // Need to inject the data we want
    lateinit var filterID: String
    val categoryDetail by lazy {   repository.categoryDetail(filterID.toInt())}
}