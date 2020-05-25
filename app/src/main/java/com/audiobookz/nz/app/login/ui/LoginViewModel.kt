package com.audiobookz.nz.app.login.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.LoginRepository
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    var logInResult = MediatorLiveData<Result<UserData>>()
    var emailResult = MediatorLiveData<Result<SuccessData>>()

    fun loginEmail(username: String, password: String) {
        logInResult.addSource(repository.loginEmail(username, password)) { value ->
            logInResult.value = value
        }
    }

    fun resetPass(email:String) {
        emailResult.addSource(repository.resetPass(email)) { value ->
            emailResult.value = value
        }
    }
}