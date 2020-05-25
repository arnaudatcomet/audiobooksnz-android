package com.audiobookz.nz.app.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
class AuthInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithToken = chain.request().newBuilder().addHeader(
                "Authorization", "Bearer K9FioHyGOjWT1rjEiwjLIuWU34M8C1bJ").build()
        val request = chain.request().newBuilder().build()
        if(chain.request().header("No-Authentication") !=null )
        {
            return chain.proceed(requestWithToken)
        }
        return chain.proceed(request)
    }
}
