package com.audiobookz.nz.app.browse.featured.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentFeaturedBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_featured.*
import javax.inject.Inject


class FeaturedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FeaturedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        context ?: return binding.root

        viewModel.fetchCategory()

        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentFeaturedBinding) {
        viewModel.featuredListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    binding.progressBar.hide()
                    result.data.let {

                        val mainAdapter = FeaturedTypeAdapter(result.data!!)
                        mainRecyclerView.adapter = mainAdapter
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

    companion object {
        fun newInstance(): FeaturedFragment =
            FeaturedFragment()
    }

}