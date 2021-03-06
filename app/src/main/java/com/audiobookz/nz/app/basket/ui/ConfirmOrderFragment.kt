package com.audiobookz.nz.app.basket.ui

import android.app.Dialog
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
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.databinding.FragmentConfirmOrderBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
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
    private var totalPrice = 0F
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
        binding.proceedPay = clickPay(binding)
        subscribeUi(binding, adapter)
        return binding.root
    }

    private fun clickPay(binding: FragmentConfirmOrderBinding): View.OnClickListener {
        return View.OnClickListener {
//            if (orderId != null)
//                if (binding.useCreditBox.isChecked) {
//                    viewModel.orderCheckout(orderId!!, "1")
//                } else {
//                    viewModel.orderCheckout(orderId!!, "0")
//                }

            var dialog = activity?.let { it1 -> Dialog(it1) }
            dialog?.setContentView(R.layout.card_form_layout)
            var lp: WindowManager.LayoutParams = WindowManager.LayoutParams().apply {
                copyFrom(dialog?.window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            val submit = dialog?.findViewById<View>(R.id.submit) as TextView
            val cardNo = dialog.findViewById<View>(R.id.cardNo) as EditText
            val month = dialog.findViewById<View>(R.id.month) as EditText
            val year = dialog.findViewById<View>(R.id.year) as EditText
            val cvv = dialog.findViewById<View>(R.id.cvv) as EditText

            submit.setOnClickListener {
                when {
                    cardNo.length() == 0 || month.length() == 0 || year.length() == 0 || cvv.length() == 0 ->
                        Toast.makeText(
                            activity, "Please fill all the fields", Toast.LENGTH_SHORT
                        ).show()
                    cardNo.length() < 16 -> Toast.makeText(
                        activity, "Please enter" +
                                " valid Card No.", Toast.LENGTH_SHORT
                    ).show()
                    else -> {

                        if (orderId != null)
                            if (binding.useCreditBox.isChecked) {
                                validateCard(
                                    cardNo.text.toString(),
                                    month.text.toString(),
                                    year.text.toString(),
                                    cvv.text.toString(),
                                    "1"
                                )
                            } else {
                                validateCard(
                                    cardNo.text.toString(),
                                    month.text.toString(),
                                    year.text.toString(),
                                    cvv.text.toString(),
                                    "0"
                                )
                            }

                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
            dialog.window?.attributes = lp
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
                println("create token Exception $e")
                Toast.makeText(
                    activity, "${e.message}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: Token) {
                println("create token result $result")
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
                Result.Status.LOADING -> { binding.progressBar.show()
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
                    //                            if (result.data.approval_url != "") {
                    //                                val navController = Navigation.findNavController(view!!)
                    //                                navController.navigate(
                    //                                    ConfirmOrderFragmentDirections.actionConfirmOrderFragmentToPayPalWebViewFragment(
                    //                                        result.data.approval_url, "Order"
                    //                                    )
                    //                                )
                    //
                    //                            }
                            }
                            "completed" -> {
                                AsyncTask.execute {
                                    viewModel.deleteCartAll()
                                }
                                Toast.makeText(
                                    activity,
                                    "completed",
                                    Toast.LENGTH_SHORT
                                ).show();3
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
                Result.Status.LOADING -> { binding.progressBar.show()
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
                Result.Status.LOADING -> { binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })
    }

}
