package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val dao: ProfileDataDao,
    private val remoteSource: ProfileRemoteDataSource
) {
    fun showProfile(firstname: String, lastname: String, email: String) = resultLiveData(
        databaseQuery = { dao.getProfileData() },
        networkCall = { remoteSource.queryProfile(firstname, lastname, email) },
        saveCallResult = { dao.insertProfileData(it) },
        nukeAudiobookList = {}
    )
}