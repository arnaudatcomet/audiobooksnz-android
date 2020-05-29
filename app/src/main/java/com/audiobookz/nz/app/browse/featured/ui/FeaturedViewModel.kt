package com.audiobookz.nz.app.browse.featured.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.browse.featured.data.FeaturedRepository
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import javax.inject.Inject


class FeaturedViewModel @Inject constructor(private val repository: FeaturedRepository) :
    ViewModel() {

    val featuredList by lazy {   repository.getFeatured(CATEGORY_PAGE_SIZE)}

//
//    var featuredListResult = MediatorLiveData<Result<List<Featured>>>()
//    var page: Int? = 1
//    var isLatest: Boolean? = false
//
//    fun fetchCategory(page: Int, pageSize: Int) {
//        featuredListResult.addSource(repository.getFeatured(CATEGORY_PAGE_SIZE)) { value ->
//            if (value.data?.size != null) {
//                featuredListResult.value = getBestResult(featuredListResult.value,value)
//            }
//        }
//    }
//    fun getBestResult(newResult: Result<List<Category>>): Result<List<Category>> {
//
//        if(bestResult!=null)
//        {
//            return Result.success(bestResult);
//        }
//        return newResult;
//    }
}