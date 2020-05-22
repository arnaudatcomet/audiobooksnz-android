package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import javax.inject.Inject


class ProfileRemoteDataSource @Inject constructor(private val service: AudiobookService) :
    BaseDataSource() {
    suspend fun sendToken(token: String) = getResult { service.getProfile(token) }

}