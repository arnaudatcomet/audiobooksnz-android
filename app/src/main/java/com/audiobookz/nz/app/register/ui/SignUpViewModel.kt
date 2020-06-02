package com.audiobookz.nz.app.register.ui


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.register.data.SignUpRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: SignUpRepository) : ViewModel() {
    var registerResult = MediatorLiveData<Result<UserData>>()
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
}
