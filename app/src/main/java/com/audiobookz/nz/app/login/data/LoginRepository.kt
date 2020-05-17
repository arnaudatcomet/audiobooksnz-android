package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val dao:UserDataDao,private val remoteSource:LoginRemoteDataSource){
    fun loginEmail(Username:String,Password:String)= resultLiveData(
        databaseQuery = {dao.getUserData()},
        networkCall = {remoteSource.emailLogin(Username,Password)},
        saveCallResult = {dao.insertUserData(it)},
        nukeAudiobookList = {}
    )
}