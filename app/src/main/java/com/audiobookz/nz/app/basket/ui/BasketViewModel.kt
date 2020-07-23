package com.audiobookz.nz.app.basket.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.basket.data.BasketRepository
import com.audiobookz.nz.app.basket.data.OrderData
import com.audiobookz.nz.app.basket.data.PaymentData
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.util.WEB_URL
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject


class BasketViewModel @Inject constructor(private val repository: BasketRepository) : ViewModel() {
    val basketResult by lazy {   repository.loadBasket()}
    var resultPayment = MediatorLiveData<Result<PaymentData>>()
    var resultOrder = MediatorLiveData<Result<OrderData>>()
    fun deleteCartById(id:Int) {
        repository.deleteBook(id)
    }

    fun orderCheckout(orderId: Int, creditUse: String) {
        var requestCancel = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/cart/paypal_fail"
        )
        var requestReturn = RequestBody.create(
            MediaType.parse("text/plain"),
            "$WEB_URL/cart/paypal_success"
        )
        //if in confirm order checkbox use credit is true useCredit = 1, default 0
        var requestUseCredit = RequestBody.create(MediaType.parse("text/plain"), creditUse)

        resultPayment.addSource(
            repository.orderCheckout(orderId, requestCancel, requestReturn, requestUseCredit)
        ) { value -> resultPayment.value = value }
    }

    fun orderBookList(orderItem: ArrayList<BookRoom>, code: String, coupon: String?) {
        resultOrder.addSource(
            repository.orderBookList(orderItem, code, coupon)
        ) { value -> resultOrder.value = value }
    }
}
