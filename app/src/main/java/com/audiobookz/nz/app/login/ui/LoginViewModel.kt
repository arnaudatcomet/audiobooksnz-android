package com.audiobookz.nz.app.login.ui

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.LoginRepository
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {
    var logInResult = MediatorLiveData<Result<UserData>>()
    var loginGoogleResult = MediatorLiveData<Result<UserData>>()
    var loginFacebookResult = MediatorLiveData<Result<UserData>>()
    var emailResult = MediatorLiveData<Result<SuccessData>>()


    fun loginEmail(username: String, password: String) {
        logInResult.addSource(repository.loginEmail(username, password)) { value ->
            logInResult.value = value
        }
    }

    fun resetPass(email: String) {
        emailResult.addSource(repository.resetPass(email)) { value ->
            emailResult.value = value
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            if (account != null){
                loginGoogleResult.addSource(repository.loginGoogle(account.idToken!!,"1")){ value ->
                    loginGoogleResult.value = value
                }
            }

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    fun facebookToken(token:String){
        loginFacebookResult.addSource(repository.loginFacebook(token,"1")){ value ->
            loginFacebookResult.value = value
        }
    }

}