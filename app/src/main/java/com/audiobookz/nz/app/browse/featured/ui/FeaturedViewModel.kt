package com.audiobookz.nz.app.browse.featured.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.browse.featured.data.FeaturedRepository
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.audiobookz.nz.app.util.FEATURED_PAGE_SIZE
import javax.inject.Inject


class FeaturedViewModel @Inject constructor(private val repository: FeaturedRepository) :
    ViewModel() {

    // val featuredList by lazy {   repository.getFeatured(CATEGORY_PAGE_SIZE)}


    var featuredListResult = MediatorLiveData<Result<Map<String, List<Featured>>?>>();
    var errorMessage = MediatorLiveData<Boolean>()

    //    var page: Int? = 1
//    var isLatest: Boolean? = false
//
    fun fetchCategory(code: String) {
        featuredListResult.addSource(repository.getFeatured(FEATURED_PAGE_SIZE)) { value ->
            if (value.data?.size != null) {
                if (value.status != Result.Status.ERROR) {
                    var resultFilter = filterCountry(code, value)
                    featuredListResult.value = getBestResult(resultFilter)
                } else {
                    errorMessage.value = true
                }

            }
        }
    }

    fun getBestResult(newResult: Result<List<Featured>?>): Result<Map<String, List<Featured>>?> {
        var data = newResult.data.let {
            it?.groupBy { it.type }
        }
        var bestResult = Result.success(data)
        return bestResult;
    }

    fun filterCountry(code: String, rawResult: Result<List<Featured>>): Result<List<Featured>?> {
        var result = rawResult.data?.filter { item ->
            item.audiobook?.audioengine_data?.active_products?.retail!!.any {
                it.territories!!.any {
                    it == code
                }
            }
        }

        return Result.success(result)
    }
}