package com.audiobookz.nz.app.register.data

import com.audiobookz.nz.app.data.resultSimpleLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(private val remoteSource: SignUpRemoteDataSource) {
    fun emailSignUp(
        email: String,
        lastName: String,
        password: String,
        terms: String,
        cPassword: String,
        firstName: String
    ) = resultSimpleLiveData(
        networkCall = {
            remoteSource.signUpWithEmail(
                email,
                lastName,
                password,
                terms,
                cPassword,
                firstName
            )
        },
        saveCallResult = {},
        onCallSuccess = {})
}