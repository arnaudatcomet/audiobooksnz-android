package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val dao:UserDataDao,private val remoteSource:LoginRemoteDataSource){
    fun loginEmail(Username:String,Password:String)= resultSimpleLiveData(
        networkCall = {remoteSource.emailLogin(Username,Password)},
        saveCallResult = {})

    fun resetPass(email: String) = resultSimpleLiveData(
        networkCall = {remoteSource.forgotPass(email)},
        saveCallResult = {dao.insertCheckMailData(it)}
    )


}