package com.audiobookz.nz.app.register.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.register.data.SignUpRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(repository: SignUpRepository): ViewModel(){
    lateinit var email:String
    lateinit var lastName:String
    lateinit var password:String
    lateinit var terms:String
    lateinit var cPassword:String
    lateinit var firstName:String
    val emailSignUp by lazy { repository.emailSignUp(email,lastName, password, terms, cPassword, firstName) }
}