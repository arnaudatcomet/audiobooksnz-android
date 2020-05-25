package com.audiobookz.nz.app.api

import com.audiobookz.nz.app.audiobookList.data.AudiobookList
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.util.*

/**
 * Lego REST API access points
 */
interface AudiobookService {

    companion object {
        const val ENDPOINT = "https://audiobooksnz.co.nz/backend/v1/"
    }

    @GET("categories")
    suspend fun getCategory(
        @Query("expand") expand: String? = null,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("filter[parent_id]") filter: Int? = 0
    ): Response<List<Category>>

    @GET("audiobooks")
    suspend fun getAudiobooksList(
        @Query("filter[category_id]") filter: Int? = null,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("filter[language][]") pageLanguage: String
    ): Response<List<AudiobookList>>

    @FormUrlEncoded
    @POST("users/login")
    suspend fun postEmailLogin(
        @Field("username") Username: String? = null,
        @Field("password") Password: String? = null
    ): Response<UserData>

    @FormUrlEncoded
    @POST("users")
    suspend fun postRegister(
        @Field("email") email: String? = null,
        @Field("lastName") lastName: String? = null,
        @Field("password") password: String? = null,
        @Field("terms") terms: String? = null,
        @Field("cPassword") cPassword: String? = null,
        @Field("firstName") firstName: String? = null
    ): Response<UserData>

    @FormUrlEncoded
    @POST("users/google-login")
    suspend fun postLoginGoogle(
        @Field("id_token") id_token: String? = null,
        @Field("device_type") device_type: String? = null
    ): Response<UserData>

    @FormUrlEncoded
    @POST("users/facebook-login")
    suspend fun postLoginFacebook(
        @Field("id_token") id_token: String? = null,
        @Field("device_type") device_type: String? = null
    ): Response<UserData>

    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String? = null
    ): Response<UserData>

    @Multipart
    @POST("users/modify")
    suspend fun editProfile(
        @Header("Authorization") token: String? = null,
        @Part image: MultipartBody.Part
        ): Response<UserData>
        
    @FormUrlEncoded
    @POST("users/request-password-reset")
    suspend fun getResetPass(
        @Field("email") email: String? = null
    ): Response<SuccessData>


//    @GET("lego/sets/")
//    suspend fun getSets(@Query("page") page: Int? = null,
//                        @Query("page_size") pageSize: Int? = null,
//                        @Query("theme_id") themeId: Int? = null,
//                        @Query("ordering") order: String? = null): Response<ResultsResponse<LegoSet>>
//
//    @GET("lego/sets/{id}/")
//    suspend fun getSet(@Path("id") id: String): Response<LegoSet>

}
