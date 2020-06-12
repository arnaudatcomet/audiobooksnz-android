package com.audiobookz.nz.app.profile.ui

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.audiobookz.nz.app.App
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.profile.data.ProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class EditProfileViewModel  @Inject constructor(private val repository: ProfileRepository, application: Application): AndroidViewModel(application){
    var editProfileResult = MediatorLiveData<Result<UserData>>()
    val queryProfile by lazy { repository.queryProfile() }
    val destroyProfile by lazy { repository.destroyProfile()

        AsyncTask.execute {
            getApplication<App>()?.let {
                AppDatabase.getInstance(
                    it
                ).userDataDao().logout()
            }
        }
    }

    fun editProfile(image:String?, firstname:String, lastname:String, currentPassword:String,newPassword:String, confirmPassword:String) {


        val file = File(image)

        val fbody: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )

        val bodypartImage = MultipartBody.Part.createFormData("imgFile", file.name, fbody)

        val nfirstname: RequestBody = RequestBody.create(MediaType.parse("text/plain"), firstname)
        val nlastname: RequestBody = RequestBody.create(MediaType.parse("text/plain"), lastname)
        val ncurrentPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), currentPassword)
        val nnewPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), newPassword)
        val nconfirmPassword: RequestBody = RequestBody.create(MediaType.parse("text/plain"), confirmPassword)

        if(image.equals("null")){
            editProfileResult.addSource(repository.editProfile(null,nfirstname, nlastname, ncurrentPassword,nnewPassword,nconfirmPassword)){value->
                editProfileResult.value = value
            }
        }else{
            editProfileResult.addSource(repository.editProfile(bodypartImage,nfirstname, nlastname, ncurrentPassword,nnewPassword,nconfirmPassword)){value->
                editProfileResult.value = value
            }
        }
    }
}