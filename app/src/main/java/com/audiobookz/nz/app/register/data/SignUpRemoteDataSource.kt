package com.audiobookz.nz.app.register.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import okhttp3.RequestBody
import javax.inject.Inject

class SignUpRemoteDataSource @Inject constructor(private val service: AudiobookService) :
    BaseDataSource() {
    suspend fun signUpWithEmail(
        email: String,
        lastName: String,
        password: String,
        terms: String,
        cPassword: String,
        firstName: String
    ) = getResult { service.postRegister(email, lastName, password, terms, cPassword, firstName) }

    suspend fun signUpPro(
        cancel_url: RequestBody,
        success_url: RequestBody

    ) = getResult { service.signUpPro(cancel_url, success_url) }
}