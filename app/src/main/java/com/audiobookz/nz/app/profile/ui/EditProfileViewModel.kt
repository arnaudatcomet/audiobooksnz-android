package com.audiobookz.nz.app.profile.ui

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.profile.data.ProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class EditProfileViewModel  @Inject constructor(private val repository: ProfileRepository): ViewModel(){
    var editProfileResult = MediatorLiveData<Result<UserData>>()

    fun editProfile(Token:String,Image:String) {


        val file = File(Image)

        val fbody: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )
        val body =
            MultipartBody.Part.createFormData("imgFile", file.name, fbody)


        editProfileResult.addSource(repository.editProfile(Token, body)){value->
            editProfileResult.value = value
        }
    }
}