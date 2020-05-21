package com.audiobookz.nz.app.profile.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.profile.data.ProfileRepository
import javax.inject.Inject

<<<<<<< Updated upstream
class ProfileViewModel @Inject constructor(repository: ProfileRepository): ViewModel(){
     var token: String?=null
    val showProfile by lazy { token?.let { repository.showProfile(it) } }
=======
class ProfileViewModel @Inject constructor(repository: ProfileRepository) : ViewModel() {
    var token: String? = null
    val queryProfile by lazy { token?.let { repository.queryProfile(it) } }
>>>>>>> Stashed changes
}