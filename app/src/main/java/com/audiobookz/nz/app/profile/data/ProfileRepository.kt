package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData

import com.audiobookz.nz.app.login.data.UserDataDao
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: UserDataDao,
    private val remoteSource: ProfileRemoteDataSource
) {
    fun queryProfile() = resultLiveData(
        databaseQuery = {dao.getUserData()},
        networkCall = {remoteSource.sendToken()},
        saveCallResult = {dao.insertUserData(it)},
        nukeAudiobookList = {}
    )
    fun editProfile(Image: MultipartBody.Part, firstname:RequestBody, lastname:RequestBody, currentPassword:RequestBody,newPassword:RequestBody, confirmPassword:RequestBody)= resultSimpleLiveData(
        networkCall = {remoteSource.sendProfileData(Image, firstname, lastname, currentPassword, newPassword, confirmPassword)},
        saveCallResult = {dao.insertUserData(it)})
}