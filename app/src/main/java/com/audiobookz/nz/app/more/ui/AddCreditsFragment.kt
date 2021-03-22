package com.audiobookz.nz.app.more.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAddCreditsBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardListData
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddCreditsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    private var tempOrderId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentAddCreditsBinding.inflate(inflater, container, false)
        binding.buyNow = clickBuyCredits(binding)
        subscribeUi(binding)
        return binding.root
    }

    private fun clickBuyCredits(binding: FragmentAddCreditsBinding): View.OnClickListener {
        return View.OnClickListener {
            var credits = binding.creditEditText.text.toString()
            val localCountryCode = context!!.resources.configuration.locale.country

            if (credits.isNotBlank()) {
                viewModel.buyCredits(credits, localCountryCode)
            } else {
                Toast.makeText(activity, "field is blank", Toast.LENGTH_SHORT).show();3
            }
        }
    }

    private fun subscribeUi(binding: FragmentAddCreditsBinding) {
        viewModel.resultBuyCredits.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    tempOrderId = result.data?.id
                    viewModel.getLocalCard()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    if (result.message == "Network :  400 Bad Request") {
                        Toast.makeText(
                            activity,
                            "Subscribe to buy more book credits (BC)",
                            Toast.LENGTH_SHORT
                        ).show();3
                    } else {
                        result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                    }
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        viewModel.statusNotification(
                            "Payment Status",
                            " Your Payment is successful"
                        )
                        val intent = Intent(
                            activity,
                            MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        //never go back if done
                        activity?.finish()
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
                        viewModel.orderCheckout(tempOrderId!!, "0", cloudCard.default!!)

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
