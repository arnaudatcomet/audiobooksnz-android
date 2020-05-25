package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData

import com.audiobookz.nz.app.login.data.UserDataDao
import okhttp3.MultipartBody
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
    fun editProfile(Image: MultipartBody.Part)= resultSimpleLiveData(
        networkCall = {remoteSource.sendProfileData(Image)},
        saveCallResult = {dao.insertUserData(it)})
}