package com.audiobookz.nz.app.login.ui
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.LoginRepository
import com.audiobookz.nz.app.login.data.UserData
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    var logInResult = MediatorLiveData<Result<UserData>>()
    fun loginEmail(username:String,password:String) {
        logInResult.addSource(repository.loginEmail(username, password)){value->
            logInResult.value = value
        }
    }
}