package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_libraryfragment, container, false)

    }

    private fun subscribeUi() {
        viewModel.localBookList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS ->{}
                Result.Status.LOADING ->{}

            }
        })
    }

}
