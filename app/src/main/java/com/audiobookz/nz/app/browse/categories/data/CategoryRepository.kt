package com.audiobookz.nz.app.browse.categories.data

import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class CategoryRepository @Inject constructor(private val remoteSource: CategoryRemoteDataSource) {

    fun category(page:Int,pageSize:Int) = resultFetchOnlyLiveData(
        networkCall = { remoteSource.fetchData(page,pageSize) }
    )

}
