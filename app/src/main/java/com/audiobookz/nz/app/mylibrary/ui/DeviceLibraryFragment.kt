package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentDeviceLibraryfragmentBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DeviceLibraryFragment : Fragment() ,Injectable{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DeviceLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val deviceLibraryAdapter = activity?.let { DeviceLibraryAdapter(it) }
        // Inflate the layout for this fragment
        var binding = FragmentDeviceLibraryfragmentBinding.inflate(inflater, container, false)

        subscribeUi(binding,deviceLibraryAdapter)
        return binding.root

    }

    private fun subscribeUi(binding: FragmentDeviceLibraryfragmentBinding, adapter: DeviceLibraryAdapter?) {
        viewModel.localBookList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS ->{
                    binding.progressBar.hide()
                    adapter?.submitList(result.data)
                    binding.DeviceRecyclerView.adapter = adapter
                }
                Result.Status.LOADING ->{binding.progressBar.show()}
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}
