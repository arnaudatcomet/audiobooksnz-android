package com.audiobookz.nz.app.login.data

import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.BaseDataSource
import com.audiobookz.nz.app.api.FacebookSocialService
import com.audiobookz.nz.app.browse.categories.data.Category
import com.facebook.login.Login
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.Continuation


//@Inject
//lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
//lateinit var callbackManager: CallbackManager

class SocialRemoteDataSource @Inject constructor(private val socialService: FacebookSocialService, private val audiobookService: AudiobookService): BaseDataSource(){
//    suspend fun emailLogin(Username:String,Password:String) = getResult { service.postEmailLogin(Username,Password) }
     suspend fun facebookLogin() {
        CoroutineScope(Main).launch {
//            socialService.getFacebookToken()
        }
    }

    suspend fun loginEmail(username: String, password: String) {
        withContext(IO) {
        }
    }
}