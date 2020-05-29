package com.audiobookz.nz.app.browse.featured.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

class FeaturedRemoteDataSource @Inject constructor(private val service: AudiobookService) : BaseDataSource() {

    suspend fun fetchData(pageSize:Int) = getResult { service.getFeatured("audiobook",pageSize) }
}
