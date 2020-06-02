package com.audiobookz.nz.app.register.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

class SignUpRemoteDataSource  @Inject constructor(private val service: AudiobookService): BaseDataSource(){
    suspend fun signUpWithEmail(email:String, lastName:String, password:String, terms:String, cPassword:String, firstName:String) = getResult { service.postRegister(email,lastName, password, terms, cPassword, firstName) }
}