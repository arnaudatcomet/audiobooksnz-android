package com.audiobookz.nz.app.browse.categories.data

import com.audiobookz.nz.app.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class CategoryRepository @Inject constructor(private val dao: CategoryDao,
                                              private val remoteSource: CategoryRemoteDataSource) {

    val category = resultLiveData(
        databaseQuery = { dao.getCategory() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = { dao.insertAll(it) })

}
