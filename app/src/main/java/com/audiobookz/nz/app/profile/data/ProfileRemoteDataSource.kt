package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject

<<<<<<< Updated upstream
class ProfileRemoteDataSource @Inject constructor(private val service: AudiobookService):BaseDataSource(){
    suspend fun queryProfile(token:String) = getResult { service.getProfile(token) }
=======
class ProfileRemoteDataSource @Inject constructor(private val service: AudiobookService) :
    BaseDataSource() {
    suspend fun sendToken(token: String) = getResult { service.getProfile(token) }
>>>>>>> Stashed changes
}