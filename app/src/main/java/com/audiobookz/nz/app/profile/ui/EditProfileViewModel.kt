package com.audiobookz.nz.app.profile.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.profile.data.ProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class EditProfileViewModel  @Inject constructor(private val repository: ProfileRepository,
                                                application: Application
): AndroidViewModel(application){
    var editProfileResult = MediatorLiveData<Result<UserData>>()

    fun editProfile(Image:String, firstname:String, lastname:String, currentPassword:String,newPassword:String, confirmPassword:String) {

        val file = File(Image)

        val fbody: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )

        val image =
            MultipartBody.Part.createFormData("imgFile", file.name, fbody)

        val nfirstname: RequestBody = RequestBody.create(MediaType.parse("text/plain"), firstname)
        val nlastname: RequestBody = RequestBody.create(MediaType.parse("text/plain"), lastname)
        val ncurrentPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), currentPassword)
        val nnewPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), newPassword)
        val nconfirmPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), confirmPassword)

        editProfileResult.addSource(repository.editProfile(image,nfirstname, nlastname, ncurrentPassword,nnewPassword,nconfirmPassword)){value->
            editProfileResult.value = value
        }
    }
}