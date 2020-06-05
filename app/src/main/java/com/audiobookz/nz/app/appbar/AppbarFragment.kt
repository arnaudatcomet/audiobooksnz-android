package com.audiobookz.nz.app.appbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.databinding.ActivityBrowseBinding
import com.audiobookz.nz.app.databinding.FragmentTopbarBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import javax.inject.Inject

class AppbarFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AppbarViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = FragmentTopbarBinding.inflate(inflater, container, false)

        context ?: return binding.root
        viewModel.count.observe(viewLifecycleOwner, Observer { count ->
            binding.count = count.toString()
        })
        return binding.root
    }
}