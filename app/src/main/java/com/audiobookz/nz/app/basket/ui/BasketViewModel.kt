package com.audiobookz.nz.app.basket.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.basket.data.BasketRepository
import com.audiobookz.nz.app.basket.data.OrderData
import com.audiobookz.nz.app.basket.data.PaymentData
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.login.data.UserData
import com.audiobookz.nz.app.util.WEB_URL
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class BasketViewModel @Inject constructor(private val repository: BasketRepository) : ViewModel() {
    val basketResult by lazy { repository.loadBasket() }
    var resultPayment = MediatorLiveData<Result<PaymentData>>()
    var resultOrder = MediatorLiveData<Result<OrderData>>()
    var resultCheckCredit = MediatorLiveData<Result<UserData>>()
    fun deleteCartById(id: Int) {
        repository.deleteBook(id)
    }

    //when pay finish
    fun deleteCartAll() {
        repository.deleteBookAll()
    }

    fun orderCheckout(orderId: Int, creditUse: String, stripeToken: String) {
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

        var requestCard = RequestBody.create(MediaType.parse("text/plain"), "0")
        var requestSaveCard = RequestBody.create(MediaType.parse("text/plain"), "0")
        var requestStripeToken = RequestBody.create(MediaType.parse("text/plain"), stripeToken)


        resultPayment.addSource(
            repository.orderCheckout(
                orderId,
                requestCancel,
                requestReturn,
                requestUseCredit,
                requestCard,
                requestSaveCard,
                requestStripeToken
            )
        ) { value -> resultPayment.value = value }
    }

    fun orderBookList(orderItem: ArrayList<BookRoom>, code: String, coupon: String?) {
        var listProduct = ArrayList<MultipartBody.Part>()

        for (i in orderItem.indices) {
            listProduct.add(
                MultipartBody.Part.createFormData(
                    "OrderItem[$i][product_id]",
                    orderItem[i].id.toString()
                )
            )
            listProduct.add(MultipartBody.Part.createFormData("OrderItem[$i][type_id]", "1"))
        }

        var requestCode = RequestBody.create(MediaType.parse("text/plain"), code)
        var requestCoupon = RequestBody.create(MediaType.parse("text/plain"), coupon)

        resultOrder.addSource(
            repository.orderBookList(listProduct, requestCoupon, requestCode)
        ) { value -> resultOrder.value = value }
    }

    fun statusNotification(title: String, body: String) =
        repository.statusNotification(title, body)

    fun getCredits() {
        resultCheckCredit.addSource(repository.getCredits()) { value ->
            resultCheckCredit.value = value
        }
    }

}
