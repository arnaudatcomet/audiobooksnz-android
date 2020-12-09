package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import okhttp3.RequestBody
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

    fun loginGoogle(token:String, device:String)=
        resultSimpleLiveData(
            networkCall = { remoteSource.loginGoogle(token, device) },
            saveCallResult = { it.access_token?.let { it1 -> sharePref.saveToken(it1) } },
            onCallSuccess = {})

    fun loginFacebook(token:String, device:String)=
        resultSimpleLiveData(
            networkCall = { remoteSource.loginFacebook(token, device) },
            saveCallResult = { it.access_token?.let { it1 -> sharePref.saveToken(it1) } },
            onCallSuccess = {})

    fun upgradePro(
        token: String,
        cancel_url: RequestBody,
        success_url: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = {
            sharePref.saveToken(token)
            remoteSource.upgradePro(cancel_url, success_url)
        })

}