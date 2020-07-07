package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultMergeMultiNetworkCallLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import com.audiobookz.nz.app.login.data.UserData

import com.audiobookz.nz.app.login.data.UserDataDao
import com.audiobookz.nz.app.mylibrary.data.AudioEngineDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.Arrays.asList
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: UserDataDao,
    private val remoteSource: ProfileRemoteDataSource,
    private val sharePref: SharedPreferencesService,
    private val audioEngineDataSource: AudioEngineDataSource
) {

    fun queryProfile() = resultMergeMultiNetworkCallLiveData(
        databaseQuery = {dao.getUserData()},
        listNetworkCall = listOf(suspend{ remoteSource.sendToken() },{remoteSource.getCredit()}),
        listSaveCallResult =  listOf<suspend (Any) -> Unit>({dao.insertUserData(it as UserData)},{ (it as UserData).let { data->data.credit_count }?.let { credit ->
            dao.updateUsersCredit(credit)
        } })
    )

    fun destroyProfile() = sharePref.deleteToken()


    fun editProfile(Image: MultipartBody.Part?, firstname:RequestBody, lastname:RequestBody, currentPassword:RequestBody,newPassword:RequestBody, confirmPassword:RequestBody)= resultSimpleLiveData(
        networkCall = {remoteSource.sendProfileData(Image, firstname, lastname, currentPassword, newPassword, confirmPassword)},
        saveCallResult = {dao.insertUserData(it)},
        onCallSuccess = {})

}