package com.audiobookz.nz.app.register.ui


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.register.data.SignUpRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: SignUpRepository) : ViewModel() {

//        lateinit var email:String
//    lateinit var lastName:String
//    lateinit var password:String
//    lateinit var terms:String
//    lateinit var cPassword:String
//    lateinit var firstName:String
//    val emailSignUp by lazy { repository.emailSignUp(email,lastName, password, terms, cPassword, firstName) }

    var RegisterResult = MediatorLiveData<Result<UserData>>()
    fun emailSignUp(
        email: String,
        lastname: String,
        password: String,
        terms: String,
        cPassword: String,
        firstname: String
    ) {
        RegisterResult.addSource(
            repository.emailSignUp(
                email,
                lastname,
                password,
                terms,
                cPassword,
                firstname
            )
        ) { value ->
            RegisterResult.value = value
        }
    }
}
