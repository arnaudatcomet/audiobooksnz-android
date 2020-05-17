package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(private val service: AudiobookService): BaseDataSource(){
    suspend fun emailLogin(Username:String,Password:String) = getResult { service.postEmailLogin(Username,Password) }
}