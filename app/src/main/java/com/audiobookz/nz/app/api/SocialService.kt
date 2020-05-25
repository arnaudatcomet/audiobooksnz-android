package com.audiobookz.nz.app.api

import android.app.Activity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookSocialService {
    companion object {
        private var instance: FacebookSocialService? = null

        fun getInstance(): FacebookSocialService? {
                synchronized(FacebookSocialService::class.java) {
                    if (instance == null) {
                        instance = FacebookSocialService()

//                        instance?.facebookCallbackManager = CallbackManager.Factory.create()
//                        LoginManager.getInstance().registerCallback(instance?.facebookCallbackManager, object :
//                            FacebookCallback<LoginResult?> {
//                            override fun onSuccess(loginResult: LoginResult?) {
////                            onSuccess(loginResult)
//                                println("ARNAUD loginResult $loginResult")
//                            }
//
//                            override fun onCancel() {
//                                println("ARNAUD onCancel")
////                          onCancel()
//                            }
//
//                            override fun onError(exception: FacebookException) {
//                                println("ARNAUD onError $exception")
////                   onError(exception)
//                            }
//                        })
                    }
                }
            return instance
        }
    }

    var facebookCallbackManager: CallbackManager = CallbackManager.Factory.create()

    fun showLogin(
        activity: Activity, list: List<String>,
        onSuccess: (LoginResult?) -> Unit /*,
                                 onCancel: () -> Void,
                                 onError: (exception: FacebookException) -> Void */ ) {
        LoginManager.getInstance().logInWithReadPermissions(activity, list)

        facebookCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(facebookCallbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
//                onSuccess(loginResult)
                println("ARNAUD loginResult $loginResult")
            }


            override fun onCancel() {
                println("ARNAUD onCancel")
            }

            override fun onError(exception: FacebookException) {
               println("ARNAUD onError $exception")
            }
        })
    }
}