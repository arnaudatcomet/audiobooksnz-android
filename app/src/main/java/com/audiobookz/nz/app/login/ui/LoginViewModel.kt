package com.audiobookz.nz.app.login.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.audiobookList.data.AudiobookListRepository
import com.audiobookz.nz.app.login.data.LoginRepository
import javax.inject.Inject


class LoginViewModel @Inject constructor(repository: LoginRepository) : ViewModel() {
    // Need to inject the data we want
    lateinit var Username: String
    lateinit var Password: String
    val loginEmail by lazy {   repository.loginEmail(Username, Password)}
}