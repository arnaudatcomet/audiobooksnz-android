package com.audiobookz.nz.app.basket.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.databinding.FragmentConfirmOrderBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar

class ConfirmOrderFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
    private var totalPrice = 0F
    private val args: ConfirmOrderFragmentArgs by navArgs()
    private lateinit var bookListProduct: ArrayList<BookRoom>
    private var orderId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentConfirmOrderBinding.inflate(inflater, container, false)
        val adapter = OrderAdapter()
        binding.orderRecycleView.adapter = adapter
        binding.proceedPay = clickPay(binding)
        subscribeUi(binding, adapter)
        return binding.root
    }

    private fun clickPay(binding: FragmentConfirmOrderBinding): View.OnClickListener {
        return View.OnClickListener {
            if (orderId != null)
                if (binding.useCreditBox.isChecked) {
                    viewModel.orderCheckout(orderId!!, "1")
                } else {
                    viewModel.orderCheckout(orderId!!, "0")
                }
        }
    }

    private fun subscribeUi(binding: FragmentConfirmOrderBinding, adapter: OrderAdapter) {
        viewModel.basketResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }
                    if (result.data?.isNotEmpty()!!) {
                        bookListProduct = result.data as ArrayList<BookRoom>
                        binding.subTotalLinear.visibility = View.VISIBLE
                        for (book in result.data) {
                            if (book.price != null)
                                totalPrice += book.price.toFloat()
                        }
                        binding.subTotalPriceOrderTxt.text = totalPrice.toString()
                        val localCountryCode = context!!.resources.configuration.locale.country
                        viewModel.orderBookList(bookListProduct, localCountryCode, args.coupon)

                    } else {
                        binding.subTotalLinear.visibility = View.GONE
                    }

                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultOrder.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    orderId = result.data?.id
                    viewModel.getCredits()
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();3
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data != null) {
                        if (result.data.state == "Temporary (checkout pending)") {
                            if (result.data.approval_url != "") {
                                val navController = Navigation.findNavController(view!!)
                                navController.navigate(
                                    ConfirmOrderFragmentDirections.actionConfirmOrderFragmentToPayPalWebViewFragment2(
                                        result.data.approval_url
                                    )
                                )
                            }
                        } else if (result.data.state == "completed") {
                            Toast.makeText(
                                activity,
                                "completed",
                                Toast.LENGTH_SHORT
                            ).show();3
                        } else {
                            Toast.makeText(
                                activity,
                                "Payment Status " + result.data.msg,
                                Toast.LENGTH_SHORT
                            ).show();3
                        }

                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();3
                }
            }
        })

        viewModel.resultCheckCredit.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data != null) {
                        binding.orderCreditsTxt.text =
                            "Use Box Credits Box(${result.data.credit_count} available)"
                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();3
                }
            }
        })
    }

}