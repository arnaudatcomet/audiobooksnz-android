package com.audiobookz.nz.app.browse.categories.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject


/**
 * Works with the Lego API to get data.
 */
class CategoryRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun fetchData(page:Int,pageSize:Int) = getResult { service.getCategory("children",page,pageSize,0) }
}
