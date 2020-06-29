package com.audiobookz.nz.app.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Kotlin extensions for dependency injection
 */

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    //xok : bind viewmodel to activity to avoid viewmodel got destroyed when destroy fragment
    return ViewModelProvider(requireActivity(), factory)[T::class.java]
}
