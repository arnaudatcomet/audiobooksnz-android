package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.addCreditClick = goToAddCredits()
        binding.wishListClick = goToWiishList()
        return binding.root
    }

    private fun goToAddCredits(): View.OnClickListener{
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreFragmentToAddCreditsFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun goToWiishList(): View.OnClickListener{
        return View.OnClickListener {
            val direction = MoreFragmentDirections.actionMoreFragmentToWishlistFragment()
            it.findNavController().navigate(direction)
        }
    }

}
