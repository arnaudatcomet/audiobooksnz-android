package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.lifecycle.Observer
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
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
        viewModel.upgradePro()
        subscribeUi()
        return inflater.inflate(R.layout.fragment_upgrade_pro, container, false)
    }

    private fun subscribeUi() {
        viewModel.resultUpgrade.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data != null) {

                        val navController = Navigation.findNavController(view!!)
                        navController.navigate(
                            UpgradeProFragmentDirections.actionUpgradeProFragmentToPayPalWebViewFragment(
                                result.data.approval_link,
                                "UpgradePro"
                            )
                        )
                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {

                    if (result.message == "Network :  400 Bad Request") {
                        AlertDialogsService(context!!).simple(
                            "Validate",
                            "You already have an active subscription"
                        )
                    } else {
                        result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                    }

                }
            }
        })
    }

}
