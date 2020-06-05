package com.audiobookz.nz.app.basket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBasketBinding
import com.audiobookz.nz.app.databinding.FragmentBookDetailBinding
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentBasketBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val adapter = BasketAdapter()
        binding.basketRecycleView.adapter = adapter

        subscribeUi(binding,adapter)
        return binding.root
    }
    private fun subscribeUi(binding: FragmentBasketBinding, adapter: BasketAdapter) {
        viewModel.basketResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }
                 //   binding.book = result.data
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