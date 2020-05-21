package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.data.resultLiveData

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
        nukeAudiobookList = {}
    )
}