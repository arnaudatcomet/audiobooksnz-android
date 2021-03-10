package com.audiobookz.nz.app.more.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.bumptech.glide.Glide


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
        binding.addCreditClick = goToAddCredits()
        binding.wishListClick = goToWishList()
        binding.currentPlanClick = goToCurrentPlan()
        binding.upgradeProClick = goToUpgrade()
        binding.listPaymentMethodClick = goToListPaymentMethods()
        viewModel.checkSubscript()
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
            val direction = MoreFragmentDirections.actionMoreToUpgradeProFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun goToListPaymentMethods(): View.OnClickListener {
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreToListCreditCardFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun subscribeUi(binding: FragmentMoreBinding) {
        viewModel.resultCheckSubscript.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data?.isSubscribed == true) {
                        binding.upgradeProCard.visibility = View.INVISIBLE
                    } else {
                        binding.upgradeProCard.visibility = View.VISIBLE
                    }

                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) };
                }
            }
        })
    }
}
