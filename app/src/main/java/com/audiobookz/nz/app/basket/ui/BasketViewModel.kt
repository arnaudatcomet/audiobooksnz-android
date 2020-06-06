package com.audiobookz.nz.app.basket.ui

import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.basket.data.BasketRepository
import com.audiobookz.nz.app.bookdetail.data.BookDetailRepository
import javax.inject.Inject


class BasketViewModel @Inject constructor(private val repository: BasketRepository) : ViewModel() {
    val basketResult by lazy {   repository.loadBasket()}
    fun deleteCartById(id:Int) {
        repository.deleteBook(id)
    }
}
