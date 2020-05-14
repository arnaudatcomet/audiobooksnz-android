package com.audiobookz.nz.app.browse.categories.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.CategoryRepository
import javax.inject.Inject

class CategoryViewModel @Inject constructor(repository: CategoryRepository) : ViewModel() {
    val category = repository.category
}

