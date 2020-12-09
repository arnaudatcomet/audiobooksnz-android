package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentMoreBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject

class MoreFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.isSubscribed = viewModel?.getIsSubscribed
        binding.addCreditClick = goToAddCredits()
        binding.wishListClick = goToWishList()
        binding.currentPlanClick = goToCurrentPlan()
        binding.upgradeProClick = goToUpgrade()
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

}
