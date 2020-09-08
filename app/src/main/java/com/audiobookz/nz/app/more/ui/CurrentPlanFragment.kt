package com.audiobookz.nz.app.more.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.R
import androidx.lifecycle.Observer
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentCurrentPlanBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject


class CurrentPlanFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentCurrentPlanBinding.inflate(inflater, container, false)
        viewModel.getCurrentPlan()
        subscribeUi(binding)
        return binding.root
    }
    private fun subscribeUi(binding: FragmentCurrentPlanBinding) {
        viewModel.getCurrentPlanResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val adapter = PlanAdapter(viewModel)
                    adapter?.submitList(result.data)
                    binding.currentPlanRecycleView.adapter = adapter
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                }
            }
        })

    }
}
