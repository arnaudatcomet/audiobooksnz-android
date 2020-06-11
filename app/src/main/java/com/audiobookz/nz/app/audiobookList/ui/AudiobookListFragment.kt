package com.audiobookz.nz.app.audiobookList.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.App
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.browse.categories.ui.VerticalItemDecoration
import com.audiobookz.nz.app.browse.featured.data.Featured
import com.audiobookz.nz.app.browse.featured.ui.BookAdapter
import com.audiobookz.nz.app.browse.featured.ui.FeaturedFragment
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAudiobookListBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_audiobook_list.view.*
import javax.inject.Inject


class AudiobookListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AudiobookListViewModel
    private val args: AudiobookListFragmentArgs by navArgs()
    lateinit var spinnerLang: Spinner
    var defaultlang: String = "English"
    private val adapter = AudiobookListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentAudiobookListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Languages,
                android.R.layout.simple_spinner_item
            ).also { adapter ->

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }

        binding.recyclerViewCatecoryDetail.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewCatecoryDetail.adapter = adapter

        if(args.listItem!=null)
        {
            binding.progressBarDetail.hide()
            adapter.submitList(args.listItem!!.map { featured -> featured.audiobook })
        } else{

            viewModel.fetchCategory(args.id, 1, CATEGORY_PAGE_SIZE, defaultlang)

            subscribeUi(binding, adapter)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerLang = view.findViewById(R.id.spinner)
        spinnerLang?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do not nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                defaultlang = spinnerLang.selectedItem.toString()
                if (args.id != 0) {
                    viewModel.fetchCategory(args.id, 1, CATEGORY_PAGE_SIZE, defaultlang)
                }

                else{

                }

            }
        }
    }

    private fun subscribeUi(binding: FragmentAudiobookListBinding, adapter: AudiobookListAdapter) {
        viewModel.bookListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBarDetail.show()
                Result.Status.ERROR -> {
                    binding.progressBarDetail.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {
        fun newInstance(): AudiobookListFragment =
            AudiobookListFragment()
    }

}
