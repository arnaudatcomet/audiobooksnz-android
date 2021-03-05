package com.audiobookz.nz.app.register.data

import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.data.resultFetchOnlyLiveData
import com.audiobookz.nz.app.data.resultSimpleLiveData
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(
    private val remoteSource: SignUpRemoteDataSource,
    private val sharePref: SharedPreferencesService
) {
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
        saveCallResult = {
            it.access_token?.let { it1 -> sharePref.saveToken(it1) }
            it.isSubscribed?.let { it1 -> sharePref.saveIsSubscribed(it1) }
            if (it.stripe_fingerprint.isNullOrEmpty()){
                sharePref.saveCardPayment(false)
            }else{
                sharePref.saveCardPayment(true)
            }
        },
        onCallSuccess = {})

    fun signUpPro(
        token: String,
        cancel_url: RequestBody,
        success_url: RequestBody
    ) = resultFetchOnlyLiveData(
        networkCall = {
            sharePref.saveToken(token)
            remoteSource.signUpPro(cancel_url, success_url)
        })
}