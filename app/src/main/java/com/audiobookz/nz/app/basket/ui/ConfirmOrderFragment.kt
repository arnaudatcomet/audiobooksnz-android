package com.audiobookz.nz.app.basket.ui

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.databinding.FragmentConfirmOrderBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardListData
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token

class ConfirmOrderFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
    private val args: ConfirmOrderFragmentArgs by navArgs()
    private lateinit var bookListProduct: ArrayList<BookRoom>
    private var orderId: Int? = null
    private lateinit var stripe: Stripe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        stripe = Stripe(context!!, "pk_test_ng7GDPEq172S4zUNrBGxUAQQ")
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

    private fun validateCard(
        card: String,
        month: String,
        year: String,
        cvv: String,
        credit: String
    ) {
        val card =
            Card.create(
                number = card,
                cvc = cvv,
                expMonth = Integer.valueOf(month),
                expYear = Integer.valueOf(year)
            )

        stripe.createCardToken(card, callback = object : ApiResultCallback<Token> {
            override fun onError(e: Exception) {
                Toast.makeText(
                    activity, "${e.message}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: Token) {
                viewModel.orderCheckout(orderId!!, credit, result.id)
            }
        })
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
                        if (localCard != null) {
                            var defaultCard = localCard.filter { it.card_id == cloudCard.default!! }
                            if (binding.useCreditBox.isChecked) {
                                validateCard(
                                    defaultCard.first().number,
                                    defaultCard.first().exp_month,
                                    defaultCard.first().exp_year,
                                    defaultCard.first().cvc,
                                    "1"
                                )
                            } else {
                                validateCard(
                                    defaultCard.first().number,
                                    defaultCard.first().exp_month,
                                    defaultCard.first().exp_year,
                                    defaultCard.first().cvc,
                                    "0"
                                )
                            }
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
