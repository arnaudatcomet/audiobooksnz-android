package com.audiobookz.nz.app.api

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
class AuthInterceptor(private val sharePref: SharedPreferencesService) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().build()

        if(chain.request().header("No-Authentication") !=null )
        {
            var token = sharePref.getToken()
            val requestWithToken = chain.request().newBuilder().addHeader(
                "Authorization", "Bearer $token").build()


            return chain.proceed(requestWithToken)
        }
        return chain.proceed(request)
    }
}
