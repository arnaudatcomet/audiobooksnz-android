package com.audiobookz.nz.app.profile.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.profile.data.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(repository: ProfileRepository): ViewModel(){
    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var email: String
    val showProfile by lazy { repository.showProfile(firstname, lastname, email) }
}