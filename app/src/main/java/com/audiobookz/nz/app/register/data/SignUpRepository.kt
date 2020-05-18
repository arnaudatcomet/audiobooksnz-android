package com.audiobookz.nz.app.register.data

import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.login.data.LoginRemoteDataSource
import com.audiobookz.nz.app.login.data.UserDataDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(private val dao: UserDataDao, private val remoteSource: SignUpRemoteDataSource){
    fun emailSignUp(email:String, lastName:String, password:String, terms:String, cPassword:String, firstName:String)= resultLiveData(
        databaseQuery = {dao.getUserData()},
        networkCall = {remoteSource.SignUpWithEmail(email,lastName, password, terms, cPassword, firstName)},
        saveCallResult = {dao.insertUserData(it)},
        nukeAudiobookList = {}
    )
}