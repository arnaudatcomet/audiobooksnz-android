package com.audiobookz.nz.app.api
import com.audiobookz.nz.app.browse.caregoryDetail.data.CategoryDetail
import com.audiobookz.nz.app.browse.categories.data.Category
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

    @GET("categories")
    suspend fun getCategory(
        @Query("expand") expand: String? = null,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("filter[parent_id]") filter: Int? = 0
    ): Response<List<Category>>

    @GET("audiobooks?filter[category_id]={id}&page=1&per-page=20&filter[language][]=English")
    suspend fun getCategoryDetail(
        @Query("filter[category_id]") filter: Int? =0,
        @Query("page") page: Int? = null,
        @Query("per-page") pageSize: Int? = null,
        @Query("filter[language][]") pageLanguage:String
        ): Response<CategoryDetail>

//    @GET("lego/sets/")
//    suspend fun getSets(@Query("page") page: Int? = null,
//                        @Query("page_size") pageSize: Int? = null,
//                        @Query("theme_id") themeId: Int? = null,
//                        @Query("ordering") order: String? = null): Response<ResultsResponse<LegoSet>>
//
//    @GET("lego/sets/{id}/")
//    suspend fun getSet(@Path("id") id: String): Response<LegoSet>

}
