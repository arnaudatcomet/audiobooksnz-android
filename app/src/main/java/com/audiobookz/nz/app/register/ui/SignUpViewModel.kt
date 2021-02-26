package com.audiobookz.nz.app.register.ui


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.register.data.SignUpProData
import com.audiobookz.nz.app.register.data.SignUpRepository
import com.audiobookz.nz.app.util.WEB_URL
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: SignUpRepository) : ViewModel() {
    var registerResult = MediatorLiveData<Result<UserData>>()
    var resultPayment = MediatorLiveData<Result<SignUpProData>>()

    fun emailSignUp(
        email: String,
        lastName: String,
        password: String,
        terms: String,
        cPassword: String,
        firstName: String
    ) {
        registerResult.addSource(
            repository.emailSignUp(
                email,
                lastName,
                password,
                terms,
                cPassword,
                firstName
            )
        ) { value ->
            registerResult.value = value
        }
    }

    fun signUpPro(token: String) {
        var requestCancel = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/new-site/user/subscription_agreement_cancel"
        )
        var requestSuccess = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/new-site/user/subscription_agreement_success"
        )

        resultPayment.addSource(
            repository.signUpPro(token, requestCancel, requestSuccess)
        ) { value -> resultPayment.value = value }
    }
}
