package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject
class LoginRemoteDataSource @Inject constructor(private val service: AudiobookService): BaseDataSource(){
    suspend fun emailLogin(Username:String,Password:String) = getResult { service.postEmailLogin(Username,Password) }

    suspend fun forgotPass(email:String) = getResult { service.getResetPass(email) }

    suspend fun loginGoogle(token:String, device:String) = getResult{ service.postLoginGoogle(token, device)}

    suspend fun loginFacebook(token:String, device:String) = getResult{ service.postLoginFacebook(token, device)}
}