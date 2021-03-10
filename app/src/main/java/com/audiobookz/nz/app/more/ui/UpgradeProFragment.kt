package com.audiobookz.nz.app.more.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentUpgradeProBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardListData
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class UpgradeProFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentUpgradeProBinding.inflate(inflater, container, false)
        binding.subscriptionClick = subscription()
        subscribeUi(binding)
        return binding.root
    }

    private fun subscription(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.getLocalCard()
        }
    }

    private fun subscribeUi(binding: FragmentUpgradeProBinding) {
        viewModel.resultUpgrade.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.upgradeProgressBar.visibility = View.GONE
                    if (result.data != null) {
                        viewModel.statusNotification(
                            "Payment Status",
                            " Congratulations! Subscription Successful, You can now use your Trial Book Credit(s)."
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
                    binding.upgradeProgressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.upgradeProgressBar.visibility = View.GONE
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

        viewModel.resultLocalCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.getCardList(result.data)
                }
                Result.Status.LOADING -> {
                    binding.upgradeProgressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.upgradeProgressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultGetCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    var list = result.data?.get("cloud") as CardListData
                    if (list.card?.size != 0) {
                        viewModel.upgradePro()
                    } else {
                        binding.upgradeProgressBar.visibility = View.GONE
                        AlertDialogsService(context!!).simple("Warning", "Payments Method is Empty")
                    }
                }
                Result.Status.LOADING -> {
                    binding.upgradeProgressBar.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.upgradeProgressBar.visibility = View.GONE
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })
    }

}
