package com.audiobookz.nz.app.api

import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.bookdetail.data.BookDetail
import com.audiobookz.nz.app.bookdetail.data.BookReview
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.login.data.SuccessData
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.more.data.WishListData
import com.audiobookz.nz.app.mylibrary.data.CloudBook
import com.audiobookz.nz.app.mylibrary.data.LocalBookData
import com.audiobookz.nz.app.mylibrary.data.SessionData
import com.audiobookz.nz.app.player.data.BookmarksData
import com.audiobookz.nz.app.player.data.PositionData
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET("audiobooks")
    suspend fun getSearchList(
        @Query("filter[search_data][like]") filter: String? = null,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null
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
        @Field("access_token") id_token: String? = null,
        @Field("device_type") device_type: String? = null
    ): Response<UserData>

    @GET("users/profile")
    @Headers("No-Authentication: false")
    suspend fun getProfile(
    ): Response<UserData>

    @GET("users/active-credit")
    @Headers("No-Authentication: false")
    suspend fun getCredit(
    ): Response<UserData>

    @Multipart
    @POST("users/modify")
    @Headers("No-Authentication: false")
    suspend fun editProfile(
        @Part Body: MultipartBody.Part? = null,
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
    @Headers("No-Authentication: false")
    suspend fun getBookDetail(
        @Path("id") id: Int
    ): Response<BookDetail>

    @GET("audiobooks/{id}")
    suspend fun getLocalBookDetail(
        @Path("id") id: Int
    ): Response<LocalBookData>

    @GET("audiobooks/{id}/reviews")
    suspend fun getBookReview(
        @Path("id") id: Int,
        @Query("expand") expand: String ="createdBy",
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null
    ): Response<List<BookReview>>

    @GET("users/audiobooks")
    @Headers("No-Authentication: false")
    suspend fun getCloudBook(
        @Query("expand") expand: String ="audiobook",
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("sort") sort: String? = null
    ): Response<List<CloudBook>>

    @GET("users/session-key")
    @Headers("No-Authentication: false")
    suspend fun getSession(): Response<SessionData>

    @FormUrlEncoded
    @POST("users/audiobooks/{cloudBookID}/position")
    @Headers("No-Authentication: false")
    suspend fun postChapterPosition(
        @Path("cloudBookID") bookId: Int,
        @Field("chapter") chapter: Int? = null,
        @Field("time") time: Long? = null,
        @Field("part_number") part_number: Int? = null
    ): Response<PositionData>

    @Multipart
    @POST("users/audiobooks/{cloudBookId}/bookmarks")
    @Headers("No-Authentication: false")
    suspend fun postBookmars(
        @Path("cloudBookId") cloudBookId: Int,
        @Part("chapter") chapter: RequestBody,
        @Part("time") time: RequestBody,
        @Part("subtitle") subtitle: RequestBody,
        @Part("part_number") part_number: RequestBody,
        @Part("title") title: RequestBody
    ): Response<BookmarksData>

    @GET("users/audiobooks/{cloudBookId}/bookmarks")
    @Headers("No-Authentication: false")
    suspend fun getBookmarks(
        @Path("cloudBookId") cloudBookId: Int,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null
    ): Response<List<BookmarksData>>

    @DELETE("users/audiobooks/bookmarks/{bookmarkId}")
    @Headers("No-Authentication: false")
    fun deleteBookmark(@Path("bookmarkId") bookmarkId: Int): retrofit2.Call<Unit>

    @FormUrlEncoded
    @PATCH("users/audiobooks/bookmarks/{bookmarkId}")
    @Headers("No-Authentication: false")
    suspend fun updateBookmarks(
        @Path("bookmarkId") bookmarkId: Int,
        @Field("title") title: String? = null
    ): Response<BookmarksData>

    @FormUrlEncoded
    @POST("audiobooks/{id}/reviews")
    @Headers("No-Authentication: false")
    suspend fun postBookReview(
        @Path("id") id: Int,
        @Field("comment") comment: String? = null,
        @Field("statification_rating") statification_rating: Float? = null,
        @Field("story_rating") story_rating: Float? = null,
        @Field("narration_rating") narration_rating: Float? = null
    ): Response<BookReview>

    @GET("wish-list")
    @Headers("No-Authentication: false")
    suspend fun getWishList(
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null
    ): Response<List<WishListData>>

    @Multipart
    @POST("wish-list")
    @Headers("No-Authentication: false")
    suspend fun addAndRemoveWishList(
        @Part("audiobook_id") audiobook_id: RequestBody
    ): Response<WishListData>

}
