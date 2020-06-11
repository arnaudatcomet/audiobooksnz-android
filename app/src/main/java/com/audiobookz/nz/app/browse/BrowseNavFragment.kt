package com.audiobookz.nz.app.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.browse.featured.ui.FeaturedViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentBrowseNavBinding
import com.audiobookz.nz.app.databinding.FragmentFeaturedBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class BrowseNavFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBrowseNavBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }
    companion object {
        fun newInstance(): BrowseNavFragment =
            BrowseNavFragment()
    }

}