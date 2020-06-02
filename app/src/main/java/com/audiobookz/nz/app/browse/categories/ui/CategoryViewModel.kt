package com.audiobookz.nz.app.browse.categories.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.categories.data.CategoryRepository
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import javax.inject.Inject

class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) :
    ViewModel() {
    var categoryResult = MediatorLiveData<Result<List<Category>>>()
    var page: Int? = 1
    var isLatest: Boolean? = false
    fun fetchCategory(page: Int, pageSize: Int) {
        categoryResult.addSource(repository.category(page, pageSize)) { value ->
            if (value.data?.size != null) {
                if (value.data.size < CATEGORY_PAGE_SIZE) {
                    isLatest = true
                }
                categoryResult.value = getBestResult(categoryResult.value,value)
            }
        }
    }
    fun getBestResult(oldResult:Result<List<Category>>?,newResult:Result<List<Category>>): Result<List<Category>>? {
        if(oldResult?.data?.get(0)?.id==newResult.data?.get(0)?.id){
            return oldResult;
        }
        var bestResult=  oldResult.let {
                list->
            newResult.data?.let { it1 -> list?.data?.plus(it1) }
        }
        if(bestResult!=null)
        {
            return Result.success(bestResult);
        }
        return newResult;
    }
}

