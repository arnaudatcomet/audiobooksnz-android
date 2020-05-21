package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.data.resultLiveData
<<<<<<< Updated upstream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val dao: ProfileDataDao,
    private val remoteSource: ProfileRemoteDataSource
) {
    fun showProfile(token: String) = resultLiveData(
        databaseQuery = { dao.getProfileData() },
        networkCall = { remoteSource.queryProfile(token) },
        saveCallResult = { dao.insertProfileData(it) },
=======
import com.audiobookz.nz.app.login.data.UserDataDao
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: UserDataDao,
    private val remoteSource: ProfileRemoteDataSource
) {
    fun queryProfile(token: String) = resultLiveData(
        databaseQuery = {dao.getUserData()},
        networkCall = {remoteSource.sendToken(token)},
        saveCallResult = {dao.insertUserData(it)},
>>>>>>> Stashed changes
        nukeAudiobookList = {}
    )
}