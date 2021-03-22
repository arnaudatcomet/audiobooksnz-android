package com.audiobookz.nz.app.basket.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentConfirmOrderBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardListData
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ConfirmOrderFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
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
        binding.proceedPay = clickPay()
        subscribeUi(binding, adapter)
        return binding.root
    }

    private fun clickPay(): View.OnClickListener {
        return View.OnClickListener {
            if (orderId != null)
                viewModel.getLocalCard()
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
                        val localCountryCode = context!!.resources.configuration.locale.country
                        viewModel.orderBookList(bookListProduct, localCountryCode, args.coupon)

                    } else {
                        binding.subTotalLinear.visibility = View.GONE
                    }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultOrder.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    orderId = result.data?.id
                    binding.subTotalPriceOrderTxt.text = result.data?.subtotal.toString()
                    binding.totalPriceOrderTxt.text = result.data?.total.toString()
                    viewModel.getCredits()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(activity, "Please Your Coupon is Correct", Toast.LENGTH_SHORT)
                        .show();3
                    findNavController().popBackStack()
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        when (result.data.state) {
                            "pending" -> {

                            }
                            "completed" -> {
                                viewModel.statusNotification(
                                    "Payment Status",
                                    " Your Payment is successful"
                                )
                                AsyncTask.execute {
                                    viewModel.deleteCartAll()
                                }
                                val intent = Intent(activity, MainActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                )
                                startActivity(intent)
                                //never go back if done
                                activity?.finish()
                            }
                            else -> {
                                Toast.makeText(
                                    activity,
                                    "Payment Status " + result.data.msg,
                                    Toast.LENGTH_SHORT
                                ).show();3
                            }
                        }

                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultCheckCredit.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        binding.orderCreditsTxt.text =
                            "Use Box Credits Box(${result.data.credit_count} available)"
                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultLocalCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.getCardList(result.data)
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultGetCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    var cloudCard = result.data?.get("cloud") as CardListData
                    var localCard = result.data?.get("local") as List<CardData>

                    if (cloudCard.card?.size != 0) {

                        if (binding.useCreditBox.isChecked) {
                            viewModel.orderCheckout(orderId!!, "1", cloudCard.default!!)
                            } else {
                            viewModel.orderCheckout(orderId!!, "0", cloudCard.default!!)
                            }

                    } else {
                        binding.progressBar.hide()
                        AlertDialogsService(context!!).simple("Warning", "Payments Method is Empty")
                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

    }

}
