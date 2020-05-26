package com.audiobookz.nz.app.browse.categories.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.Category
import com.audiobookz.nz.app.browse.categories.data.CategoryRepository
import com.audiobookz.nz.app.data.Result
import javax.inject.Inject

class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) : ViewModel() {
    var categoryResult = MediatorLiveData<Result<List<Category>>>()
    var page:Int?=1
    var isLastest:Boolean?=false
    fun fetchCategory (page:Int,pageSize:Int)
    {
        categoryResult.addSource( repository.category(page,pageSize)){ value ->
            if(value.status==Result.Status.LATEST)
            {
                isLastest=true
            }else{
                categoryResult.value= value

            }
        }

    }
}

