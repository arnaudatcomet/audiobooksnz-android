package com.audiobookz.nz.app.browse.categories.data

import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultPaginationLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class CategoryRepository @Inject constructor(private val dao: CategoryDao,
                                              private val remoteSource: CategoryRemoteDataSource) {

    fun category(page:Int,pageSize:Int) = resultPaginationLiveData(
        databaseQuery = { dao.getCategory() },
        networkCall = { remoteSource.fetchData(page,pageSize) },
        saveCallResult = { dao.insertAll(it) }
    )

}
