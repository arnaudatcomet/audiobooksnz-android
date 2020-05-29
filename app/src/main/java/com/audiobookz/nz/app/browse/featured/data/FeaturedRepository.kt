package com.audiobookz.nz.app.browse.featured.data

import com.audiobookz.nz.app.browse.categories.data.CategoryRemoteDataSource
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeaturedRepository @Inject constructor(private val remoteSource: FeaturedRemoteDataSource) {

    fun getFeatured(pageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.fetchData(pageSize) }
    )

}
