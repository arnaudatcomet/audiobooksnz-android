package com.audiobookz.nz.app.profile.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class ProfileRemoteDataSource @Inject constructor(private val service: AudiobookService) :
    BaseDataSource() {
    suspend fun sendToken() = getResult { service.getProfile() }

    suspend fun sendProfileData(image: MultipartBody.Part, firstname:RequestBody ,lastname:RequestBody, currentPassword:RequestBody,newPassword:RequestBody, confirmPassword:RequestBody) = getResult { service.editProfile(image, firstname,lastname, currentPassword, newPassword, confirmPassword) }

}