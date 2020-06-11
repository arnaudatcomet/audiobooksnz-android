package com.audiobookz.nz.app.api

import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
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
        @Query("filter[language][]") pageLanguage: String?="English"
    ): Response<List<Audiobook>>

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
    @Headers("No-Authentication: false")
    suspend fun getProfile(
    ): Response<UserData>

    @Multipart
    @POST("users/modify")
    @Headers("No-Authentication: false")
    suspend fun editProfile(
        @Part Body: MultipartBody.Part,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastname: RequestBody,
        @Part("oldPassword") oldPassword: RequestBody,
        @Part("newPassword") newPassword: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody
    ): Response<UserData>

    @FormUrlEncoded
    @POST("users/request-password-reset")
    suspend fun getResetPass(
        @Field("email") email: String? = null
    ): Response<SuccessData>

    @GET("featured")
    suspend fun getFeatured(
        @Query("expand") expand: String? = null,
        @Query("per-page") pageSize: Int? = null
    ): Response<List<Featured>>

    @GET("audiobooks/{id}")
    suspend fun getBookDetail(
        @Path("id") id: Int
    ): Response<BookDetail>

    @GET("audiobooks/{id}/reviews")
    suspend fun getBookReview(
        @Path("id") id: Int,
        @Query("expand") expand: String ="createdBy",
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null
    ): Response<List<BookReview>>

}
