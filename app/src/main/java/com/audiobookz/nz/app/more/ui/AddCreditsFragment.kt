package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAddCreditsBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject

class AddCreditsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentAddCreditsBinding.inflate(inflater, container, false)
        binding.buyNow = clickBuyCredits(binding)
        subscribeUi()
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

    private fun subscribeUi() {
        viewModel.resultBuyCredits.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.id?.let { viewModel.orderCheckout(it, "0") }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    if (result.message == "Network :  400 Bad Request") {
                        Toast.makeText(
                            activity,
                            "Visit our website and subscribe to our Pro Plan to buy more book credits",
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
                    if (result.data != null) {
                        if (result.data.approval_url != "") {
                            val navController = Navigation.findNavController(view!!)
                            navController.navigate(
                                AddCreditsFragmentDirections.actionAddCreditsFragmentToPayPalWebViewFragment(
                                    result.data.approval_url, "Credits"
                                )
                            )
                        } else {
                            Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();3
                        }
                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

    }

}
