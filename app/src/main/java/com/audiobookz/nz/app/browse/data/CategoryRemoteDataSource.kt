package com.audiobookz.nz.app.browse.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject


/**
 * Works with the Lego API to get data.
 */
class CategoryRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

//    suspend fun fetchData() = getResult { service.getThemes(1, 1000, "-id") }
}
