package com.audiobookz.nz.app.mylibrary.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

class MyLibraryRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun getCloudBook(Page:Int,PageSize:Int) = getResult { service.getCloudBook("audiobook",Page,PageSize) }
}