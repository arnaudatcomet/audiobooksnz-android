package com.audiobookz.nz.app.api

// Dataset to be added later
//import com.elifox.legocatalog.legoset.data.LegoSet
//import com.elifox.legocatalog.legotheme.data.LegoTheme

import com.audiobookz.nz.app.browse.data.Category
import com.elifox.legocatalog.api.ResultsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Lego REST API access points
 */
interface AudiobookService {

    companion object {
        const val ENDPOINT = "https://audiobooksnz.co.nz/backend/v1/"
    }

    @GET("categories/")
    suspend fun getCategory(
        @Query("expand") expand: String? = null,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("filter[parent_id]") filter: Int? = 0
    ): Response<ResultsResponse<Category>>

//    @GET("lego/sets/")
//    suspend fun getSets(@Query("page") page: Int? = null,
//                        @Query("page_size") pageSize: Int? = null,
//                        @Query("theme_id") themeId: Int? = null,
//                        @Query("ordering") order: String? = null): Response<ResultsResponse<LegoSet>>
//
//    @GET("lego/sets/{id}/")
//    suspend fun getSet(@Path("id") id: String): Response<LegoSet>

}
