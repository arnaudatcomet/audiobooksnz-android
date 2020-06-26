package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val remoteSource: LoginRemoteDataSource,
    private val sharePref: SharedPreferencesService
) {

    fun loginEmail(Username: String, Password: String) =
        resultSimpleLiveData(
            networkCall = { remoteSource.emailLogin(Username, Password) },
            saveCallResult = { it.access_token?.let { it1 -> sharePref.saveToken(it1) } },
            onCallSuccess = {})

    fun resetPass(email: String) = resultSimpleLiveData(
        networkCall = { remoteSource.forgotPass(email) },
        saveCallResult = {},
        onCallSuccess = {})
}