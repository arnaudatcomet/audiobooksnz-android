package com.audiobookz.nz.app.browse.categories.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.browse.categories.data.CategoryRepository
import javax.inject.Inject

class CategoryViewModel @Inject constructor(repository: CategoryRepository) : ViewModel() {
    // Need to inject the data we want
    val category = repository.category
}


//* The ViewModel for [LegoThemeFragment].
//*/
//class LegoThemeViewModel @Inject constructor(repository: LegoThemeRepository) : ViewModel() {
//
//    val legoThemes= repository.themes
//}
