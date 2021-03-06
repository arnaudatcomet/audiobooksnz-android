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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentListCreditCardBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
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
        stripe = Stripe(context!!, "pk_test_ng7GDPEq172S4zUNrBGxUAQQ")
        val binding = FragmentListCreditCardBinding.inflate(inflater, container, false)
        binding.addCard = addCard()
        viewModel.getCardList()
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
                            activity, "Please fill all the fields"
                            , Toast.LENGTH_SHORT
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
        card: String,
        month: String,
        year: String,
        cvv: String
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
                    activity, "${e.message}"
                    , Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: Token) {
                println("create token result $result")
                viewModel.addPaymentCard(result.id)
            }
        })
    }

    private fun subscribeUi(binding: FragmentListCreditCardBinding) {
        viewModel.resultGetCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val adapter = result.data?.default?.let { CardListAdapter(viewModel, it) }
                    adapter?.submitList(result.data?.card)
                    binding.creditCardRecycleView.adapter = adapter
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultAddCard.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.getCardList()
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {

                    result.message?.let {
                        AlertDialogsService(context!!).simple(
                            "Error Add Card",
                            it
                        )
                    }
                }
            }
        })
    }
}