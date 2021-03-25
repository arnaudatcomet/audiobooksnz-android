package com.audiobookz.nz.app.more.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentListCreditCardBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import javax.inject.Inject

class ListCreditCardFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    private lateinit var stripe: Stripe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        stripe = Stripe(context!!, "pk_live_N0CdLj2KJ3pon5nCACKVUlb2")
//        stripe = Stripe(context!!, "pk_test_ng7GDPEq172S4zUNrBGxUAQQ")

        val binding = FragmentListCreditCardBinding.inflate(inflater, container, false)
        binding.addCard = addCard()
        viewModel.getLocalCard()
        subscribeUi(binding)
        return binding.root
    }

    private fun addCard(): View.OnClickListener {
        return View.OnClickListener {

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
                        validateCard(
                            cardNo.text.toString(),
                            month.text.toString(),
                            year.text.toString(),
                            cvv.text.toString()
                        )
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
            dialog.window?.attributes = lp

        }
    }

    private fun validateCard(
        number: String,
        month: String,
        year: String,
        cvc: String
    ) {
        val card =
            Card.create(
                number = number,
                cvc = cvc,
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
                viewModel.addPaymentCard(result.id, number, cvc, month, year)
            }
        })
    }

    private fun subscribeUi(binding: FragmentListCreditCardBinding) {

        viewModel.resultLocalCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.getCardList(result.data)
                }
                Result.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultAddCardLocal.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.getLocalCard()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultGetCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val adapter =
                        result.data?.let {
                            CardListAdapter(viewModel, it)
                        }
                    binding.creditCardRecycleView.adapter = adapter
                }
                Result.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultAddCard.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.getLocalCard()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.message == "Network :  500 Internal Server Error") {
                        AlertDialogsService(context!!).simple("Warning", "Can't add this card")
                    }
                    else{
                        result.message?.let {
                            AlertDialogsService(context!!).simple(
                                "Error Add Card",
                                it
                            )
                        }
                    }
                }
            }
        })
    }
}