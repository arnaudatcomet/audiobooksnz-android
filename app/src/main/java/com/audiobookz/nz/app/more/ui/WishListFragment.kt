package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentWishlistBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject

class WishListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentWishlistBinding.inflate(inflater, container, false)
        viewModel.getWishList()
        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentWishlistBinding) {
        viewModel.resultGetWishList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val adapter = WishListAdapter(viewModel)
                    adapter?.submitList(result.data)
                    binding.wishListRecycleView.adapter = adapter
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                }
            }
        })

        viewModel.resultRemoveWishList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.ERROR -> {
                    if (result.message == "Network :  204 No Content") {
                        viewModel.getWishList()
                    }
                }
            }
        })

    }

}
