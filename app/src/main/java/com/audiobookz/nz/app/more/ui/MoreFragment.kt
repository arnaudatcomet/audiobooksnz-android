package com.audiobookz.nz.app.more.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.databinding.FragmentMoreBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result


class MoreFragment : Fragment(), Injectable {

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
        val binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.isSubscribed = viewModel.getIsSubscribed
        // binding.hasCard = viewModel.getHasCard
        binding.addCreditClick = goToAddCredits()
        binding.wishListClick = goToWishList()
        binding.currentPlanClick = goToCurrentPlan()
        binding.upgradeProClick = goToUpgrade()
        binding.listPaymentMethodClick = goToListPaymentMethods()
        subscribeUi(binding)
        return binding.root
    }

    private fun goToAddCredits(): View.OnClickListener {
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreFragmentToAddCreditsFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun goToWishList(): View.OnClickListener {
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreFragmentToWishlistFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun goToCurrentPlan(): View.OnClickListener {
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreToCurrentPlanFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun goToUpgrade(): View.OnClickListener {
        return View.OnClickListener {

            // val direction = MoreFragmentDirections.actionMoreToUpgradeProFragment()
            // it.findNavController().navigate(direction)

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

    private fun goToListPaymentMethods(): View.OnClickListener{
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreToListCreditCardFragment()
            it.findNavController().navigate(direction)
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
                    activity, "${e.message}"
                    , Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: Token) {
                println("create token result $result")
                viewModel.addPaymentCard(result.id,number,cvc,month,year)
            }
        })
    }

    private fun subscribeUi(binding: FragmentMoreBinding) {
        viewModel.resultAddCard.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.upgradePro()
                }
                Result.Status.LOADING -> {
                    binding.moreProgressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.moreProgressBar.visibility = View.GONE
                    result.message?.let {
                        AlertDialogsService(context!!).simple(
                            "Error Add Card",
                            it
                        )
                    }
                }
            }
        })
        viewModel.resultUpgrade.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.moreProgressBar.visibility = View.GONE
                    binding.isSubscribed = true
//                    AlertDialogsService(context!!).simple(
//                        "Success",
//                        "Payment Method Added"
//                    )
                }
                Result.Status.LOADING -> {
                    binding.moreProgressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.moreProgressBar.visibility = View.GONE
                    when (result.message) {
                        "Network :  400 Bad Request" -> {
                            AlertDialogsService(context!!).simple(
                                "Validate",
                                "You already have an active subscription"
                            )
                        }
                        "Network :  403 Forbidden" -> {
                            AlertDialogsService(context!!).simple(
                                "Validate",
                                "Sorry, This card is used for a trial plan already."
                            )
                        }
                        else -> {
                            result.message?.let {
                                AlertDialogsService(context!!).simple(
                                    "Error",
                                    it
                                )
                            }
                        }
                    }
                }
            }
        })
    }
}
