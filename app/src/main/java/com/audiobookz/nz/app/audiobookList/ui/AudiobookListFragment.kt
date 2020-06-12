package com.audiobookz.nz.app.audiobookList.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.audiobookz.nz.app.ui.setTitle
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
    var defaultLang: String = "English"
    private val adapter = AudiobookListAdapter()
    lateinit var binding: FragmentAudiobookListBinding
    private lateinit var fragmentStatus: String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentStatus = "onAttach"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentStatus == "onAttach") {
            fragmentStatus = "onViewCreated"

            args.titleList?.let { setTitle(it) }
            viewModel = injectViewModel(viewModelFactory)
            binding = FragmentAudiobookListBinding.inflate(inflater, container, false)
            context ?: return binding.root

            //set up selectLangDialog
            activity?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.Languages,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.selectLangDialog.adapter = adapter
                }
            }

            binding.recyclerViewCatecoryDetail.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.recyclerViewCatecoryDetail.adapter = adapter

            when {
                //open by showAll
                args.listItem != null -> {
                    binding.progressBarDetail.hide()
                    binding.selectLangDialog.show()
                    adapter.submitList(args.listItem!!.map { featured -> featured.audiobook })
                }
                //open by category
                args.id != 0 -> {
                    binding.selectLangDialog.show()
                    viewModel.fetchCategory(args.id, 1, CATEGORY_PAGE_SIZE, defaultLang)
                    subscribeUi(binding, adapter)
                }
                //open by search
                else -> {
                    args.keyword?.let { viewModel.fetchSearch(it, 1, CATEGORY_PAGE_SIZE) }
                    subscribeUi(binding, adapter)
                }
            }

            return binding.root
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerLang = view.findViewById(R.id.selectLang_Dialog)
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

                if (args.id != 0 && defaultLang != spinnerLang.selectedItem.toString()) {
                    defaultLang = spinnerLang.selectedItem.toString()
                    viewModel.fetchCategory(args.id, 1, CATEGORY_PAGE_SIZE, defaultLang)
                    subscribeUi(binding, adapter)
                } else {
                    //change lang in show all
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

        viewModel.searchListResult.observe(viewLifecycleOwner, Observer { result ->
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
