package com.audiobookz.nz.app.basket.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBasketBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class BasketFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
    private var totalPrice = 0F
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentBasketBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val adapter = BasketAdapter(viewModel)
        binding.basketRecycleView.adapter = adapter
        binding.goToBrowse = goToBrowse()
        binding.goToConfirm = goToConfirm(binding)
        subscribeUi(binding, adapter)
        return binding.root
    }

    private fun goToBrowse():View.OnClickListener{
        return View.OnClickListener{
            val intent = Intent(activity, MainActivity::class.java).apply {
                putExtra("basket", true)
            }
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun goToConfirm(binding: FragmentBasketBinding):View.OnClickListener{
        return View.OnClickListener{
            val coupon = binding.couponEditText.text.toString()
            val direction = BasketFragmentDirections.actionBasketFragmentToConfirmOrderFragment(coupon)
            it.findNavController().navigate(direction)
        }
    }


    private fun subscribeUi(binding: FragmentBasketBinding, adapter: BasketAdapter) {
        viewModel.basketResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }

                    //calculate total price basket
                    if (result.data?.isNotEmpty()!!) {
                        binding.subTotalLinear.visibility = View.VISIBLE
                        for (book in result.data) {
                            if (book.price != null)
                                totalPrice += book.price.toFloat()
                        }
                        binding.totalPriceTxt.text = totalPrice.toString()
                    } else {
                        binding.subTotalLinear.visibility = View.GONE
                    }

                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

    }
}