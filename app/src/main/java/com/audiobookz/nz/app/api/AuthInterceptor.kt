package com.audiobookz.nz.app.api

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {

    var shrPref = context.getSharedPreferences("Token",Context.MODE_PRIVATE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var token = shrPref.getString("Token","")

        val requestWithToken = chain.request().newBuilder().addHeader(
                "Authorization", "Bearer $token").build()
        val request = chain.request().newBuilder().build()

        if(chain.request().header("No-Authentication") !=null )
        {
            return chain.proceed(requestWithToken)
        }
        return chain.proceed(request)
    }
}
